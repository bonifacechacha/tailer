
def reader = new Tailer()
def dateTxt = new Date().format("yyyy-MM-dd")

Properties properties = new Properties()
File propertiesFile = new File('config.properties')
propertiesFile.withInputStream {
    properties.load(it)
}

def filePath = properties."file-path"


println filePath

reader.tail(new File(filePath)) { println it }

class Tailer {

  void tail (File file, Closure c) {
    def runnable = {
     def reader

      try {
        reader = file.newReader()
        reader.skip(file.length())

        def line
        def stop = false
        while (!stop) {
          line = reader.readLine()
          if (line) {
            c.call(line)
          } else {
            Thread.currentThread().sleep(1000)
          }
        }

      } finally {
        reader?.close()
      }
    } as Runnable

    def t = new Thread(runnable)
    t.start()
  }
}
