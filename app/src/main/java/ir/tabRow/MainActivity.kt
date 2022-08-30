package ir.tabRow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import ir.ktab.KTabItem
import ir.ktab.KTabRow
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(
            content = {
                val coroutineScope = rememberCoroutineScope()

                val pagerList = listOf(0, 1, 2)
                val pagerState = rememberPagerState()

                val (pagerWidth, setPagerWidth) = remember { mutableStateOf(0) }

                val scrollOffset = remember(pagerWidth) {
                    derivedStateOf {
                        (pagerState.currentPage + pagerState.currentPageOffset) * pagerWidth
                    }
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        KTabRow(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .height(height = 56.dp),
                            tabs = {
                                for (tab in pagerList)
                                    KTabItem(
                                        indicatorColor = Color.Green,
                                        onTabClick = { tabIndex ->
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(
                                                    page = tabIndex
                                                )
                                            }
                                        },
                                        index = tab,
                                        scrollOffset = scrollOffset.value,
                                        pagerWidth = pagerWidth,
                                        totalItems = pagerList.size,
                                        selectedContent = {
                                            Row(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceEvenly
                                            ) {
                                                Text(
                                                    text = "title $tab",
                                                    color = Color.White
                                                )
                                                Icon(
                                                    imageVector = Icons.Rounded.Home,
                                                    contentDescription = null,
                                                    tint = Color.White
                                                )
                                            }
                                        },
                                        unselectedContent = { modifier ->
                                            Row(
                                                modifier = modifier,
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceEvenly
                                            ) {
                                                Text(
                                                    text = "title $tab",
                                                    color = Color.Black
                                                )
                                                Icon(
                                                    imageVector = Icons.Rounded.Home,
                                                    contentDescription = null,
                                                    tint = Color.Black
                                                )
                                            }
                                        }
                                    )
                            },
                            spaceBetween = 16.dp
                        )

                        HorizontalPager(
                            modifier = Modifier
                                .onSizeChanged {
                                    setPagerWidth(it.width)
                                },
                            count = pagerList.size,
                            state = pagerState,
                            content = { page ->
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                    content = {
                                        Text(
                                            text = "${pagerList[page]}"
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}