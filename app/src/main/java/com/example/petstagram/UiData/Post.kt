package com.example.petstagram.UiData

import java.time.Instant
import java.util.Date

class Post ()
{

    /**id of the document in the DB*/
    var id : String = ""
    /**title of the post TODO still not shown in UI*/
    var title : String = ""
    /**user that posted it*/
    var creatorUser: Profile? = null
    /**category this post is at*/
    var category : Category?= null
    /**type of media in this post, by now it can only be image or video*/
    var typeOfMedia:String = ""
    /**date the post was posted, autogenerated on create*/
    var postedDate : Date = Date.from(Instant.now())
    /**URL reference to the media of the post*/
    var source : String = ""

}