# imaginer
Stop reinventing the wheel every time you need to spin up a server.   
Raag, I'm talking to you.    
Please stop.   

Imaginer attempts to blitz past possibly uninteresting boilerplate code so that the developer can get to the business logic of their web server and their website. This sort of setup plays very kindly with most cloud platforms, including AWS, Google Cloud, and Heroku. 

**Usage**   
```
Imaginer.create()
  .withPort(1234)
  .withApi("/api/*", "com.raagnair.endpoints")
  .withUI("./src/frontend")
  .go();
```

The above code will spin up an embedded Jetty web server at localhost:1234.   
All Jersey endpoints defined in "com.raagnair.endpoints" will be available at localhost:1234/api.   
All static files (HTML, JS, CSS) defined in "./src/frontend" will be available at localhost:1234.   

**Installation**  
Follow instructions here: https://jitpack.io/

Maven:   
```
<dependency>
  <groupId>com.github.raagnair</groupId>
  <artifactId>imaginer</artifactId>
  <version>master</version>
</dependency>
```

Use whatever artifact definition you need based on your build tool. I recommend against using version=master in case I'm not able to keep the master release stable. I will be cutting stable releases at a later point in time.    

**I M A G I N E R**   
**I** **M**ight **A**ctually **G**o **I**nsane **N**othing **E**ver **R**uns
