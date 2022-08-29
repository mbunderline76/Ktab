package ir.ktab

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

@Composable
fun KTabRow(
    modifier: Modifier,
    tabs: @Composable () -> Unit,
    spaceBetween: Dp
) {
    SubcomposeLayout(
        modifier = modifier,
        measurePolicy = { constraints: Constraints ->
            val tabRowWidth = constraints.maxWidth
            val tabMeasurables: List<Measurable> = subcompose(
                slotId = "Tabs",
                content = tabs
            )
            val tabCount = tabMeasurables.size
            val tabFrame = (tabRowWidth / tabCount)
            val tabWidth = tabFrame - (spaceBetween.value.toInt() * 2)
            val tabPlaceables: List<Placeable> = tabMeasurables.map {
                it.measure(
                    constraints = constraints.copy(
                        minWidth = tabWidth,
                        maxWidth = tabWidth
                    )
                )
            }
            layout(
                width = tabRowWidth,
                height = constraints.maxHeight,
                placementBlock = {
                    tabPlaceables.forEachIndexed { index, placeable ->
                        placeable.placeRelative(
                            x = (index * tabFrame) + spaceBetween.value.toInt(),
                            y = 0
                        )
                    }
                }
            )
        }
    )
}