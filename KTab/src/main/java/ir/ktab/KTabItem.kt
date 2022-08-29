package ir.ktab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@Composable
fun KTabItem(
    indicatorColor: Color,
    backGroundColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(size = 16.dp),
    onTabClick: (Int) -> Unit,
    index: Int,
    scrollOffset: Float,
    pagerWidth: Int,
    totalItems: Int,
    selectedContent: @Composable () -> Unit,
    unselectedContent: @Composable (Modifier) -> Unit
) {
    val indicatorTranslationX = (scrollOffset - (index * pagerWidth)) / totalItems

    var clipableTextStartPosition by remember {
        mutableStateOf(0F)
    }
    var clipableTextEndPosition by remember {
        mutableStateOf(0F)
    }
    var clipableTextWidth by remember {
        mutableStateOf(0F)
    }
    var indicatorStartPosition by remember {
        mutableStateOf(0F)
    }
    var indicatorWidth by remember {
        mutableStateOf(0F)
    }

    Surface(
        onClick = {
            onTabClick(index)
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = indicatorTranslationX
                            }
                            .onGloballyPositioned { layoutCoordinates ->
                                indicatorStartPosition = layoutCoordinates.positionInParent().x
                                indicatorWidth = layoutCoordinates.size.width.toFloat()
                            },
                        shape = shape,
                        content = {

                        },
                        color = indicatorColor
                    )

                    selectedContent()

                    val indicatorEndPosition = indicatorStartPosition + indicatorWidth

                    val leftClip =
                        if (indicatorEndPosition > clipableTextStartPosition && indicatorEndPosition < clipableTextEndPosition)
                            indicatorEndPosition - clipableTextStartPosition
                        else 0F

                    val rightClip =
                        if (indicatorStartPosition > clipableTextStartPosition && indicatorStartPosition < clipableTextEndPosition)
                            indicatorStartPosition - clipableTextStartPosition
                        else if (indicatorEndPosition > clipableTextStartPosition && indicatorEndPosition < clipableTextEndPosition)
                            clipableTextWidth
                        else if (indicatorEndPosition < clipableTextEndPosition || indicatorStartPosition > clipableTextEndPosition)
                            clipableTextWidth
                        else 0F

                    val unselectedContentModifier = Modifier
                        .onGloballyPositioned {
                            clipableTextStartPosition = it.positionInParent().x
                            clipableTextEndPosition =
                                it.positionInParent().x + it.size.width
                            clipableTextWidth =
                                clipableTextStartPosition + clipableTextEndPosition
                        }
                        .drawWithContent {
                            clipRect(
                                left = leftClip,
                                right = rightClip,
                                block = {
                                    this@drawWithContent.drawContent()
                                }
                            )
                        }

                    unselectedContent(unselectedContentModifier)
                }
            )
        },
        shape = shape,
        color = backGroundColor
    )
}





































































