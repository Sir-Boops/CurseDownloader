package main

import "os"
import "fmt"
import "sync"
import "time"
import "strconv"
import "strings"
import "path/filepath"
import "github.com/tidwall/gjson"
import "github.com/mholt/archiver"

func main() {

	if len(os.Args) < 2 || len(os.Args) > 2 {
		fmt.Println("You only need the url!")
		os.Exit(0)
	}

	expath, _ := filepath.Abs(os.Args[0])

	fmt.Println("Downloading modpack zip")
	WriteFile(ReadRemote(os.Args[1]+"/file"), filepath.Dir(expath)+"/gay.zip")

	archiver.Unarchive(filepath.Dir(expath)+"/gay.zip", "gay")
	os.RemoveAll(filepath.Dir(expath) + "/gay.zip")

	manifest := gjson.Parse(string(ReadTextFile(filepath.Dir(expath) + "/gay/manifest.json")))

	pack_folder := filepath.Dir(expath) + "/" + manifest.Get("name").String() + "/"
	os.Rename(filepath.Dir(expath)+"/gay/overrides", pack_folder)
	os.RemoveAll(filepath.Dir(expath) + "/gay")
	fmt.Println("")

	var wg sync.WaitGroup
	r := 0
	var failed_dls []string

	for i := 0; i < len(manifest.Get("files").Array()); i++ {
		if manifest.Get("files").Array()[i].Get("required").Bool() {

			mods_folder := pack_folder + "mods/"

			r = r + 1
			for r > 10 {
				time.Sleep(250 * time.Millisecond)
			}

			mod := manifest.Get("files").Array()[i]

			wg.Add(1)
			go func() {
				defer wg.Done()
				defer func() {
					r = r - 1
				}()
				URL := DecodeProjectId(strconv.Itoa(int(mod.Get("projectID").Int()))) +
					"/download/" + strconv.Itoa(int(mod.Get("fileID").Int())) + "/file"
				if !strings.Contains(URL, "https") {
					failed_dls = append(failed_dls, URL)
				} else {
					mod_filename := GetFileName(URL)
					if !strings.Contains(mod_filename, ".jar") && !strings.Contains(mod_filename, ".zip") {
						failed_dls = append(failed_dls, URL)
					} else {
						fmt.Println("Downloading:", mod_filename)
						os.MkdirAll(mods_folder, os.ModePerm)
						WriteFile(ReadRemote(URL), mods_folder+mod_filename)
					}
				}
			}()
		}
	}

	fmt.Println("")
	fmt.Println("==== Waiting for all downloads to finish ====")
	fmt.Println("")
	wg.Wait()

	fmt.Println("")
	for i := 0; i < len(failed_dls); i++ {
		fmt.Println("Failed to download:", failed_dls[i])
	}
	fmt.Println("")

	fmt.Println("Use Minecraft version:", manifest.Get("minecraft.version"))
	for i := 0; i < len(manifest.Get("minecraft.modLoaders").Array()); i++ {
		modloader := manifest.Get("minecraft.modLoaders").Array()[i].Get("id").String()
		if strings.Contains(modloader, "forge") {
			URL := "https://files.minecraftforge.net/maven/net/minecraftforge/forge/" + manifest.Get("minecraft.version").String() +
				"-" + strings.Split(modloader, "-")[1] + "/forge-" + manifest.Get("minecraft.version").String() + "-" + strings.Split(modloader, "-")[1] + "-installer.jar"
			WriteFile(ReadRemote(URL), pack_folder+"forge.jar")
		}
		fmt.Println("Requires modloader:", modloader)
	}
	fmt.Println("")

}
