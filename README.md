# cacoo_community

[![Build Status](https://travis-ci.org/mogaleaf/cacoo_community.svg?branch=master)](https://travis-ci.org/mogaleaf/cacoo_community)

## Purpose

This application is a backend server to rate template diagrams coming from *cacoo* users.

The user can import his diagrams into the application.
Everybody can rate a diagram.
Everybody can see the most recent diagrams.

## Configuration of the Server

* In src/java/resources Place your *ConsumerKey* and *ConsumerSecret*  
ConsumerKey=To put  
ConsumerSecret=To put

* Run mvn clean install

* run the application with **java -jar cacoo-1.0-SNAPSHOT.jar**

* the application will be up on http port 8080

## API

**Authorize the application to import your diagrams template :**

**path:** /user/new  
**Params**  

 Name | Required | Default
 ------------ | ------------- | -------------
 email | yes |

**Import your template diagrams:**

**path:**/user/import  
**Params**  

 Name | Required | Default
 ------------ | ------------- | -------------
 email | yes |

**Get the 10 most popular Diagrams**

**path:**/api/popular  
**Params**  

 Name | Required | Default
 ------------ | ------------- | -------------
 max | no | 10


**Get the 10 most recent Diagrams**

**path:**/api/recent  
**Params**  

 Name | Required | Default
 ------------ | ------------- | -------------
 max | no | 10

**Rate a Diagram**

**path:** /api/rate  
**Params**  

 Name | Required | Default
 ------------ | ------------- | -------------
 DiagId | yes | |
 score | no | 0


## Stack

 * SpringBoot  
 * Redis  
 
## Next Ideas

* https on the server  
* Add a user sign up pssiblity  
* Vote allowed only one time per user
* Having global Vote et per user Vote
* Machine learning to recommend diag based on vote
* Possibilty to import a diagram in user account 
