# Web crawler

```bash
$ docker build -t web-crawler .
$ docker run --rm -v "$(pwd)":/app -w /app web-crawler ./gradlew run
$ open app/resources/www.yahoo.co.jp/index.html
```
