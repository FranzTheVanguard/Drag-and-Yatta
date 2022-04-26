package com.example.yatta2048


import android.content.ClipData
import android.content.ClipDescription
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout



class MainActivity : AppCompatActivity() {
    lateinit var mainLayout : ConstraintLayout
    lateinit var box1 : FrameLayout ; lateinit var box2 : FrameLayout ; lateinit var box3 : FrameLayout
    lateinit var box4 : FrameLayout ; lateinit var box5 : FrameLayout ; lateinit var box6 : FrameLayout
    lateinit var box7 : FrameLayout ; lateinit var box8 : FrameLayout ; lateinit var box9 : FrameLayout
    val names = arrayOf("kiana", "mei", "senti")
    lateinit var mediaPlayer : MediaPlayer
    var sentiScore : Int = 0
    lateinit var scoreText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer.create(this, R.raw.yatta_sfx)
        mainLayout = findViewById(R.id.mainlayout)
        box1 = findViewById(R.id.box1); box2 = findViewById(R.id.box2); box3 = findViewById(R.id.box3)
        box4 = findViewById(R.id.box4); box5 = findViewById(R.id.box5); box6 = findViewById(R.id.box6)
        box7 = findViewById(R.id.box7); box8 = findViewById(R.id.box8); box9 = findViewById(R.id.box9)
        var spawnButton: Button = findViewById(R.id.spawnbutton)
        spawnButton.setOnClickListener { v: View ->
            createBlock()
        }
        scoreText = findViewById(R.id.senti_score)
    }

    private val dragListen = View.OnDragListener { v, event ->
        val receiverView : ImageView = v as ImageView
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                v.invalidate()
                true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                v.invalidate()
                true
            }

            DragEvent.ACTION_DRAG_LOCATION ->{
                v.invalidate()
                true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                v.invalidate()
                true
            }

            DragEvent.ACTION_DROP -> {
                v.invalidate()
                val item: ClipData.Item = event.clipData.getItemAt(0)
                val dragData = item.text
                var viewfinder = event.localState as ImageView
                if(dragData.equals(receiverView.tag)&&viewfinder.parent!=receiverView.parent){
                    mediaPlayer.start()
                    var parentview : FrameLayout = viewfinder.parent as FrameLayout
                    parentview.removeAllViews()
                    when(receiverView.tag){
                        "kiana" ->{
                            receiverView.setImageResource(R.drawable.yatta_mei)
                            receiverView.tag = "mei"
                        }
                        "mei"->{
                            receiverView.setImageResource(R.drawable.yatta_senti)
                            receiverView.tag = "senti"
                        }
                        "senti"->{
                            sentiScore += 1
                            scoreText.setText(" : $sentiScore")
                            val tempView : FrameLayout = receiverView.parent as FrameLayout
                            tempView.removeAllViews()
                        }
                    }
                }
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                v.invalidate()
                when(event.result) {
                    true ->{
                        // drop was handled

                    }
                    else ->{
                        // drop didn't work
                    }
                }

                // returns true; the value is ignored.
                true
            }

            else -> {
                // An unknown action type was received.
                false
            }
        }
    }

    private fun createBlock(){
        var block : ImageView = ImageView(this)
        block.tag = names[0]
        block.setOnLongClickListener { v:View ->
            val item = ClipData.Item(block.tag as? String)

            val dragData = ClipData(
                block.tag as? String,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val myShadow = View.DragShadowBuilder(v)

            v.startDragAndDrop(dragData,myShadow,v,0)
        }
        block.setOnDragListener(dragListen)
        val pixels = this.resources.displayMetrics.density * 80
        block.layoutParams = ViewGroup.LayoutParams(pixels.toInt(),pixels.toInt())
        block.setImageResource(R.drawable.yatta_kiana)
        var yatta_position : Int = 0
        if(!isFull()){
            while(getBox(yatta_position).childCount>0){
                yatta_position = (0..8).random()+1
            }
            getBox(yatta_position).addView(block)
        }
        else Toast.makeText(this, "Full!", Toast.LENGTH_SHORT).show()
    }

    private fun getBox(index : Int) : FrameLayout{
        when(index){
            1 -> return box1
            2 -> return box2
            3 -> return box3
            4 -> return box4
            5 -> return box5
            6 -> return box6
            7 -> return box7
            8 -> return box8
            9 -> return box9
        }
        return box1
    }

    private fun isFull() : Boolean{
        var totalFilled : Int = box1.childCount+box2.childCount+box3.childCount+box4.childCount+box5.childCount+box6.childCount+box7.childCount+box8.childCount+box9.childCount
        return totalFilled>=9
    }
}
