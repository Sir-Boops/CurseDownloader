package main

import "path"
import "net/http"

func DecodeProjectId(Id string) (string) {
  client := &http.Client{
    CheckRedirect: func(req *http.Request, via []*http.Request) error {
        return http.ErrUseLastResponse
    },
  }

  req, _ := http.NewRequest("GET", "https://www.curseforge.com/projects/" + Id, nil)
  req.Header.Add("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:67.0) Gecko/20100101 Firefox/67.0")
  resp, _ := client.Do(req)
  defer resp.Body.Close()

  return resp.Header.Get("Location")
}

func GetFileName(URL string) (string) {
  client := &http.Client{
    CheckRedirect: func(req *http.Request, via []*http.Request) error {
        return http.ErrUseLastResponse
    },
  }

  req, _ := http.NewRequest("GET", URL, nil)
  req.Header.Add("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:67.0) Gecko/20100101 Firefox/67.0")
  resp, _ := client.Do(req)
  defer resp.Body.Close()
  return path.Base(resp.Header.Get("Location"))
}
