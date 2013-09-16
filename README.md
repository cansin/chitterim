#Chitter.im

Chitter.im is a Gtalk bot that allows you to communicate with Twitter very simply. Using Chitter.im bot; you can follow your timeline, send (re)tweets and direct messages, (un)follow users, see incoming friendships.

##Development

Both Chitter.im bot and it's web interface is hosted at Google App Engine for Java (GAEj).

The bot itself uses XMPP service of GAEj to communicate with clients. Since, Twitter's Stream API cannot be handled at GAEj, we're polling timeline changes as well, using Task Queues and Cronjob services of GAEj. We're also using some other popular services like Bit.ly URL shortening APIs as well.

The web interface is a Java Servlet/JSP module to gather account information. It uses OAuth for Twitter, and Users service of GAEj for Gtalk authentication. The persistence is achieved using Java Data Objects (JDO) Query Language (JDOQL).

##How to Get it Work

- Install Eclipse with below plugins:
  - [Google App Engine Java SDK](https://developers.google.com/appengine/docs/java/tools/eclipse)
  - [AJDT: AspectJ Development Tools](http://www.eclipse.org/ajdt/downloads/)
  - [Eclipse Git Team Provider](http://www.eclipse.org/egit/download/)
  - Eclipse Java EE Developer Tools
  - Eclipse Java Web Developer Tools
- Update `/com/chitter/external/Config.java` file to include your secret and consumer keys for both twitter and bit.ly. 
- Add Twitter4J v2.2.5 library to `war/WEB-INF/lib` (Download from [here](http://twitter4j.org/archive/twitter4j-2.2.5.zip)).
