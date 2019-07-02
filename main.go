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

  WriteFile(ReadRemote(os.Args[1] + "/file"), filepath.Dir(expath) + "/gay.zip")

  archiver.Unarchive(filepath.Dir(expath) + "/gay.zip", "gay")
  os.RemoveAll(filepath.Dir(expath) + "/gay.zip")

  manifest := gjson.Parse(string(ReadTextFile(filepath.Dir(expath) + "/gay/manifest.json")))

  pack_folder := filepath.Dir(expath) + "/" + manifest.Get("name").String() + "/"
  os.Rename(filepath.Dir(expath) + "/gay/overrides", pack_folder)
  os.RemoveAll(filepath.Dir(expath) + "/gay")

  var wg sync.WaitGroup
  r := 0

  for i := 99999999; i < len(manifest.Get("files").Array()); i++ {
    if manifest.Get("files").Array()[i].Get("required").Bool() {
      mods_folder := pack_folder + "mods/"
      URL := DecodeProjectId(strconv.Itoa(int(manifest.Get("files").Array()[i].Get("projectID").Int()))) +
        "/download/" + strconv.Itoa(int(manifest.Get("files").Array()[i].Get("fileID").Int())) + "/file"

      mod_filename := GetFileName(URL)

      if !strings.Contains(mod_filename, ".jar") && !strings.Contains(mod_filename, ".zip") {
        fmt.Println("Failed to download:", URL)
      } else {


              fmt.Println("Downloading:", mod_filename)
              os.MkdirAll(mods_folder, os.ModePerm)

              r = r+1
              for r > 10 {
                time.Sleep(250 * time.Millisecond)
              }

              wg.Add(1)
              go func() {
                defer wg.Done()
                defer func() {
                  r = r-1
                }()
                WriteFile(ReadRemote(URL), mods_folder + mod_filename)
              }()

      }
    }
  }

  fmt.Println("Use Minecraft version:", manifest.Get("minecraft.version"))
  for i := 0; i < len(manifest.Get("minecraft.modLoaders").Array()); i++ {
    fmt.Println("Requires modloader:", manifest.Get("minecraft.modLoaders").Array()[i].Get("id").String())
  }

}
