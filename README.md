Play twitter search
===================

A Play project to search tweets using twitter4j.

Cross domains requests are allowed, by default.

## Configuration

Configure twitter4j via [System properties](http://twitter4j.org/en/configuration.html)

```
-Dtwitter4j.oauth.consumerKey=*********************
-Dtwitter4j.oauth.consumerSecret=******************************************
-Dtwitter4j.oauth.accessToken=**************************************************
-Dtwitter4j.oauth.accessTokenSecret=******************************************
```

Change the `conf/application.conf` file according to your needs:

#### twitter.keywords

Type: `Array[String]`

The list of keywords to search

#### twitter.limit

Type: `Int`

The limit of tweets to retrieve

#### twitter.cache.ttlInSeconds

Type: `Int`

The cache time-to-live value

#### twitter.matchText

Type: `String`
_optional_

The text to search in the tweet text

## Endpoints

* `GET /tweets`
* `GET /tweets/since/:id`

## Tweet example

```json
{
    id: 1,
    createdAt: 1310034422000,
    url: "https://twitter.com/???/status/???",
    text: "foo",
    favoriteCount: 0,
    retweetCount: 0,
    source: "<a href="http://twitter.com/#!/download/ipad" rel="nofollow">Twitter for iPad</a>",
    user: {
        id: 1,
        name: "bar",
        screenName: "qix",
        profileImageURL: "http://pbs.twimg.com/profile_images/???/???.jpeg",
    profileURL: "https://twitter.com/???"
    },
    matches: false
}
```

The default configuration retrieves the last 25 tweets from the SNCF.