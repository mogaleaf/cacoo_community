# cacoo_community

[![Build Status](https://travis-ci.org/mogaleaf/cacoo_community.svg?branch=master)](https://travis-ci.org/mogaleaf/cacoo_community)

## Purpose

This application is a backend server to rate template diagrams coming from *cacoo* users.

The user can import his diagrams into the application.
Everybody can rate a diagram.
Everybody can see the most recent diagrams.

## Try It  

Test the deployed version on Heroku [ here ](https://safe-mesa-68420.herokuapp.com/).  
As the redis is embedded and heroku kills the app when not used, data are lost when the application is sleeping.

## Configuration of the Server

* In src/java/resources Place your *ConsumerKey* and *ConsumerSecret*   or set System environment variable with the same name.
ConsumerKey=To put  
ConsumerSecret=To put

* Run mvn clean install

* run the application with **java -jar cacoo-1.0-SNAPSHOT.jar**

* the application will be up on http port 8080 ( http://localhost:8080 )

## Authentication

**Signup**  

**path:** /signin   
**Return** : a sessionId required to import diagrams.  

## User API

**Import your template diagrams:**

**path:**/user/import  
**Params**  

 Name | Required | Default
 ------------ | ------------- | -------------
 sessionId | yes |

## Public API

**Get the 10 most popular Diagrams**

**path:**/api/popular  
**Params**  

 Name | Required | Default
 ------------ | ------------- | -------------
 max | no | 10

**Return**  
Json format of diagram  
```
[
{
name: "elec",
id: "m2D4SThi4NeZEeQA",
imageUrl: "https://cacoo.com/diagrams/m2D4SThi4NeZEeQA.png",
rate: 5,
numberOfRate: 1
},
{
name: "test",
id: "LZWPrWaUeBewflTT",
imageUrl: "https://cacoo.com/diagrams/LZWPrWaUeBewflTT.png",
rate: 0,
numberOfRate: 0
}
]
```

**Get the 10 most recent Diagrams**

**path:**/api/recent  
**Params**  

 Name | Required | Default
 ------------ | ------------- | -------------
 max | no | 10  
 
 **Return**  
   
Json format of diagram  
```
[
{
name: "elec",
id: "m2D4SThi4NeZEeQA",
imageUrl: "https://cacoo.com/diagrams/m2D4SThi4NeZEeQA.png",
rate: 5,
numberOfRate: 1
},
{
name: "test",
id: "LZWPrWaUeBewflTT",
imageUrl: "https://cacoo.com/diagrams/LZWPrWaUeBewflTT.png",
rate: 0,
numberOfRate: 0
}
]
```


**Rate a Diagram**

**path:** /api/rate  
**Params**  

 Name | Required | Default
 ------------ | ------------- | -------------
 DiagId | yes | |
 score | no | 0


## Stack

 * Java 8
 * SpringBoot  
 * Redis  
 * Bootstrap
 * Javascript
 
## Next Steps  
  
* https on the server  

## Next Ideas

* Vote allowed only one time per user
* Having global Vote and per user Vote
* Machine learning to recommend diag based on vote
* Possibilty to import a diagram in user account 
