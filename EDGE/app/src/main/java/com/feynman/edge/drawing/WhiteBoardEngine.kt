package com.feynman.edge.drawing

class WhiteBoardEngine {
    private val slides: MutableList<Slide> =mutableListOf(Slide())
    private var currentSlide: Int=0
    private var activeStroke: Stroke?=null
    private var redo: MutableList<Stroke> =mutableListOf()

    fun getCurrentSlide(): Slide=slides[currentSlide]
    fun getSlideCount(): Int=slides.size
    fun getCurrentSlideIndex():Int=currentSlide
    fun getActiveStrokeForRender(): Stroke? = activeStroke
    fun getSlideAt(index: Int): Slide = slides[index]
    fun nextSlide(){
        if(currentSlide<slides.size-1) currentSlide++
        else{
            slides.add(Slide())
            currentSlide++
        }
        redo.clear()
    }
    fun prevSlide(){
        if(currentSlide>0){
            currentSlide--
            redo.clear()
        }
    }

    fun startStroke(x:Float,y:Float,tool: Tool,color: Int){
        val stroke=Stroke(tool=tool,color=color)
        stroke.points.add(Point(x,y))
        activeStroke=stroke
    }
    fun addPoint(x: Float,y: Float){
        activeStroke?.points?.add(Point(x,y))
    }
    fun finishStroke(){
        activeStroke?.let {
            getCurrentSlide().strokes.add(it)
            redo.clear()
        }
        activeStroke=null
    }

    fun doundo(){
        val slide=getCurrentSlide()
        if(slide.strokes.isNotEmpty()){
            val removed=slide.strokes.removeAt(slide.strokes.size-1)
            redo.add(removed)
        }
    }
    fun doredo(){
        if(redo.isNotEmpty()){
            val stroke=redo.removeAt(redo.size-1)
            getCurrentSlide().strokes.add(stroke)

        }
    }
    fun clear(){
        getCurrentSlide().strokes.clear()
        redo.clear()
    }
    fun resetAllSlides() {
        slides.clear()
        slides.add(Slide())
        currentSlide = 0
        activeStroke = null
        redo.clear()
    }


}