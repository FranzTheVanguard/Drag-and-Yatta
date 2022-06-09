package com.example.yatta2048


import android.content.ClipData
import android.content.ClipDescription
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.yatta2048.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var mainLayout : ConstraintLayout
    private lateinit var
            box1 : FrameLayout ; private lateinit var box2 : FrameLayout ; private lateinit var box3 : FrameLayout
    private lateinit var box4 : FrameLayout ; private lateinit var box5 : FrameLayout ; private lateinit var box6 : FrameLayout
    private lateinit var box7 : FrameLayout ; private lateinit var box8 : FrameLayout ; private lateinit var box9 : FrameLayout
    private lateinit var spawnButton : Button
    private val names = arrayOf("kiana", "mei", "senti")
    private lateinit var mediaPlayer : MediaPlayer
    private var sentiScore : Int = 0
    private lateinit var scoreText : TextView
    private var _binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAssign()


    }

    private fun initAssign() {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        mediaPlayer = MediaPlayer.create(this, R.raw.yatta_sfx)
        mainLayout = _binding!!.mainlayout
        box1 = _binding!!.box1; box2 = _binding!!.box2; box3 = _binding!!.box3
        box4 = _binding!!.box4; box5 = _binding!!.box5; box6 = _binding!!.box6
        box7 = _binding!!.box7; box8 = _binding!!.box8; box9 = _binding!!.box9
        spawnButton = _binding!!.spawnbutton
        assignOnClick()
        scoreText = _binding!!.sentiScore
    }

    private fun assignOnClick() {
        spawnButton.setOnClickListener {
            createBlock()
        }
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
                val viewfinder = event.localState as ImageView
                if(dragData.equals(receiverView.tag)&&viewfinder.parent!=receiverView.parent){
                    mediaPlayer.start()
                    val parentview : FrameLayout = viewfinder.parent as FrameLayout
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
                            scoreText.text = " : $sentiScore"
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
        var block = ImageView(this)
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
        var yattaPosition = 0
        if(!isFull()){
            while(getBox(yattaPosition).childCount>0){
                yattaPosition = (0..8).random()+1
            }
            getBox(yattaPosition).addView(block)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
