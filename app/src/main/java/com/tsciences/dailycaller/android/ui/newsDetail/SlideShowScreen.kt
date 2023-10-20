package com.tsciences.dailycaller.android.ui.newsDetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.google.android.gms.ads.*
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.util.popActivity
import com.tsciences.dailycaller.android.data.remote.home.Slide
import com.tsciences.dailycaller.android.ui.commonComponents.*
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlideShowScreen(
    slides: List<Slide>, onShareClick: () -> Unit
) {
    val context = LocalContext.current

    SlideShowScreenContent(
        navigateBack = context::popActivity, slides = slides, onShareClick = onShareClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SlideShowScreenContent(
    navigateBack: () -> Unit, slides: List<Slide>, onShareClick: () -> Unit
) {
    val currentPage = mutableStateOf(0)
    val pagerState = rememberPagerState()
    LaunchedEffect(pagerState.currentPage) {
        // do your stuff with pagerState.currentPage
        currentPage.value = pagerState.currentPage
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            CloseButton(onClick = navigateBack)
            ShareButton(onClick = {
                onShareClick()
            })
        }


        HorizontalPager(
            modifier = Modifier.weight(2f), pageCount = slides.size, state = pagerState
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                slides.get(currentPage.value).slideTitle?.let { it1 ->
                    Text(
                        text = it1,
                        modifier = Modifier.weight(.5f),
                        fontFamily = FontFamily(Font(R.font.sofia_pro_medium)),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                AsyncImage(
                    model = slides.get(currentPage.value).slideFullImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(3f)
                )

            }
        }
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (leftArrow, rightArrow, currentCount, totalCount, slash) = createRefs()
            if (!currentPage.value.equals(0)) {
                IconButton(modifier = Modifier.constrainAs(leftArrow) {
                    start.linkTo(parent.start)
                }, onClick = {
                    currentPage.value = currentPage.value - 1
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.content_back_arrow),
                        tint = Color.White,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.back_button))
                    )
                }
            }

            if (!currentPage.value.equals(slides.size - 1)) {
                IconButton(modifier = Modifier.constrainAs(rightArrow) {
                    end.linkTo(parent.end)
                }, onClick = {
                    currentPage.value = currentPage.value + 1
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = stringResource(R.string.contact_us),
                        tint = Color.White,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.back_button))
                    )
                }
            }

            Text(text = (currentPage.value + 1).toString(),
                fontFamily = FontFamily(Font(R.font.roboto_light)),
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.constrainAs(currentCount) {
                    end.linkTo(slash.start)
                })
            Text(text = "/",
                fontFamily = FontFamily(Font(R.font.roboto_light)),
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(slash) {
                        start.linkTo(currentCount.end)
                        end.linkTo(totalCount.start)
                        centerHorizontallyTo(parent)
                    }
                    .padding(start = 10.dp, end = 10.dp))
            Text(text = slides.size.toString(),
                fontFamily = FontFamily(Font(R.font.roboto_light)),
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.constrainAs(totalCount) {
                    start.linkTo(slash.end)
                })
        }
    }
}







