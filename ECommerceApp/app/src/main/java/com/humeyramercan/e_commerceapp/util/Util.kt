package com.humeyramercan.e_commerceapp.util

import android.content.Context
import android.graphics.Paint
import android.util.Patterns
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.workmanager.SendNotificationWorker
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

fun ImageView.downloadFromUrl(url:String,progressDrawable: CircularProgressDrawable){

    val options = RequestOptions()
        .placeholder(progressDrawable) //the drawable that is shown while data are loading
        .error(R.mipmap.ic_launcher_round) //default error drawable

        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(this)
}

fun placeHolderProgressBar(context:Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth=8f
        centerRadius=40f
        start()
    }

}
fun TextInputEditText.validEmail():Boolean{
    val textInputLayout=this.parent.parent as TextInputLayout
    val input=this.text.toString()
    if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()){
        textInputLayout.helperText="Please enter a valid email address"
        return false
    }else{
        textInputLayout.helperText=""
    }
    return true
}
fun TextInputEditText.validPhone():Boolean{
    val textInputLayout=this.parent.parent as TextInputLayout
    val input=this.text.toString()
    if (!input.matches(".*[0-9].*".toRegex())) {
        textInputLayout.helperText= "All must be digits."
        return false
    }
    if (input.length != 10) {
        textInputLayout.helperText="Must be 10 digits."
        return false
    }
    if(input.length==10&&input.matches(".*[0-9].*".toRegex())){
        textInputLayout.helperText=""
    }
    return true
}

fun TextInputEditText.validPassword():Boolean{
    val textInputLayout=this.parent.parent as TextInputLayout
    val input=this.text.toString()
    if(input.length<8){
        textInputLayout.helperText="Must be minimum 8 character."
        return false
    }
    if(input.length>16){
        textInputLayout.helperText="Must be maximum 16 character."
        return false
    }
    if(input.length in 8..16){
        textInputLayout.helperText=""
    }
    return true
}

fun TextInputEditText.validNameOrSurname():Boolean{
    val textInputLayout=this.parent.parent as TextInputLayout
    val input=this.text.toString()
    if(input.isBlank()){
        textInputLayout.helperText="Required field."
        return false
    }
    if(input.length<2){
        textInputLayout.helperText="Must be minimum 2 character."
        return false
    }
    if(input.isNotBlank() && input.length>2){
        textInputLayout.helperText=""
    }
    return true
}

fun TextInputEditText.validPasswordForUpdate():Boolean{
    val textInputLayout=this.parent.parent as TextInputLayout
    val input=this.text.toString()
    if(input.isBlank()){
        return true
    }
    if(input.length in 1..7){
        textInputLayout.helperText="Must be minimum 8 character."
        return false
    }
    if(input.length>16){
        textInputLayout.helperText="Must be maximum 16 character."
        return false
    }
    if(input.length in 8..16){
        textInputLayout.helperText=""
    }
    return true
}

fun strikeOutText(textView: TextView, amount: Double) { //For salePrice text.
    textView.text = amount.toString() + "$"
    textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
}

fun convertToTwoDigitsAfterDot(amount: Double): String { //To round sale prices down.
    val df = DecimalFormat("#.##")
    return df.format(amount)
}


