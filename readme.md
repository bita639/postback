[![Build Status](https://travis-ci.org/pimg/postback.svg?branch=master)](https://travis-ci.org/pimg/postback)

#Postback

##Postback is a http push framework using callback url subscriptions.
This is an early pre-alpha release where not all functionality is implemented.
It uses a Rest API for managing subscriptions and EHCache for persisting state of the subscriptions.

##Use cases
![alt text](https://raw.githubusercontent.com/pimg/postback/master/docs/Postback_use_cases.jpg "use cases")

##Sequence diagram
![alt text](https://raw.githubusercontent.com/pimg/postback/master/docs/Postback_sequence%20diagrams.jpg "sequence diagrams")

###TODO
This is an early pre-alpha release where not all functionality is implemented.
- [x] Create Rest API for managaging subscriptions
- [x] Add seperate Threadpool to send messages to different subscribers
- [ ] Change timer to JMS subscriber
- [ ] Extend exception handling to cleanup "dead" subscriptions
- [ ] Add functionality so no duplicate subscriptions can occur in the cache
- [ ] Add functionality to have specific functional subscriptions (and multiple caches)
