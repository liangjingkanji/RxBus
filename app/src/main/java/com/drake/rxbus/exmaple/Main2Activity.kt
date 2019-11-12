package com.drake.rxbus.exmaple

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drake.rxbus.sendEvent
import com.drake.rxbus.sendTag
import com.drake.tooltip.toast
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)



        btn_send_tag.setOnClickListener {
            sendTag("refresh_event")

            toast("已发送标签: refresh_event")
        }


        btn_send_event.setOnClickListener {
            val event = Event("金城武")
            sendEvent(event, "refresh_event")

            toast("已发送事件: $event")
        }

    }
}
