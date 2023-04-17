package com.Corestep.androidapp.utility

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.google.firebase.dynamiclinks.DynamicLink

import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2

object DeepLinkUtils {

  private  const val coreStepWebsiteUrl = "https://www.example.com/"
  private  const val coreStepDeeplinkUrl = "https://example.page.link"
  private  const val coreStepAndroidDomainName = "com.example.androidapp"
  private  const val coreStepIosDomainName = "com.example.iosapp"


    fun generateDeepLink(customerId: String, action: (String?) -> Unit){
    val dynamicLink =    Firebase.dynamicLinks.createDynamicLink()
            .setLink(Uri.parse(coreStepWebsiteUrl+customerId))
            .setDomainUriPrefix(coreStepDeeplinkUrl)
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .setIosParameters(DynamicLink.IosParameters.Builder(coreStepIosDomainName).build())
            .buildDynamicLink()

       Firebase.dynamicLinks.createDynamicLink()
            .setLongLink(dynamicLink.uri)
            .buildShortDynamicLink()
            .addOnCompleteListener {
                action.invoke(it.result?.shortLink.toString())
            }
            .addOnFailureListener {
                println(it)
                it.localizedMessage?.let { it1 -> Log.e("DEEPLINKUTILS", it1) }
            }
    }


    fun getReferralCode(activity: Activity, action:(String?)->Unit) {
        Firebase.dynamicLinks.getDynamicLink(activity.intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                if (pendingDynamicLinkData != null){
                    val  deepLink = pendingDynamicLinkData.link
                    val mystring = deepLink.toString().substring(deepLink.toString().indexOf("m/") + 2, deepLink.toString().length)
                    action.invoke(mystring)
                } else {
                    action.invoke(null)
                }
            }.addOnFailureListener {
                action.invoke(null)
            }
    }
}