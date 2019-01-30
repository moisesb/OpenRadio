package net.moisesborges

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import net.moisesborges.di.HelloWorldProvider
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val helloWorldProvider: HelloWorldProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.hello_text_view).text = helloWorldProvider.helloWorld()
    }
}
