# RealIMSample
Instant messaging sample made possible with several amazing technologies.


#Backend
The backend consists of two parts:
##[Firebase](www.firebase.com) 
Which is the main component of the backend, its reliable and one of the fastest realtime 
backend services out there. It is used for storing all chatrooms' information like name, users, messages and image urls
and broadcasting all the changes to users in the chatroom.
##[AmazonS3](aws.amazon.com/s3/)
It is an online file storage web service that provide fast cloud storage all over the world. It is used for storing all
images taken and it's url is sent back so it could be part of the message that is sent to firebase

