package main

import "os"
import "io/ioutil"

func CheckForFile(Path string) (bool) {
  // True if found
  // False if not
  ans := false
  if _, err := os.Stat(Path); err == nil {
    // File/Folder found!
    ans = true
  }
  return ans
}

func WriteFile(Data []byte, Path string) {
  // Deletes the file before writing to it
  if CheckForFile(Path) {
    os.Remove(Path)
  }
  ioutil.WriteFile(Path, Data, os.ModePerm)
}

func ReadTextFile(Path string) ([]byte) {
  bytes, _ := ioutil.ReadFile(Path)
  return bytes
}
