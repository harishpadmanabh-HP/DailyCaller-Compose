package com.tsciences.dailycaller.android.ui.commonComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.appConstants.AppConstants
import com.tsciences.dailycaller.android.core.theme.BorderWidthDefault
import com.tsciences.dailycaller.android.core.theme.SpacingXSmall
import com.tsciences.dailycaller.android.core.util.getCategoryColor
import com.tsciences.dailycaller.android.core.util.getTime
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.ui.commonComponents.utils.dailyCallerScreenContentPadding
import com.tsciences.dailycaller.android.utils.stripHtml

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNewsItem(
    news: Item,
    onNewsItemClick: (Item) -> Unit,
) {
    Surface(onClick = { onNewsItemClick(news) }) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
                .padding(dailyCallerScreenContentPadding())
                .padding(top = 5.dp, bottom = 5.dp),

            ) {
            val (newsImage, categoryTag, title, authorName, whiteDotImage, time, authorImage, patriotImage) = createRefs()
            AsyncImage(model = news.imageLargeUrl
                ?: news.image?.loc?.text,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.tsciences.dailycaller.android.R.dimen.home_firstItemHeight))
                    .constrainAs(newsImage) {},
                placeholder = painterResource(id = R.drawable.default_image_wide)
            )

            if (news.premiumContent == true) {
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                        .constrainAs(patriotImage) {
                            start.linkTo(newsImage.start, 10.dp)
                            bottom.linkTo(newsImage.bottom, 10.dp)
                        },
                    painter = painterResource(id = com.tsciences.dailycaller.android.R.drawable.patriots),
                    contentDescription = "Section Gradient",
                )
            }

            Text(
                text = stripHtml(news.title),
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(newsImage.bottom, 5.dp)
                    start.linkTo(parent.start, 10.dp)
                },
                maxLines = 2,
                fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.sofia_pro_semibold)),
                fontSize = 14.sp,
                color = Color.Black
            )

            Text(text = news.categoryTag.toString(),

                modifier = Modifier
                    .background(
                        color = getCategoryColor(news.categoryTag.toString()),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .constrainAs(categoryTag) {
                        top.linkTo(title.bottom, 5.dp)
                        start.linkTo(parent.start, 10.dp)
                    }
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White)
            Text(
                text = news.authorName.toString(),

                modifier = Modifier.constrainAs(authorName) {
                    top.linkTo(title.bottom, 5.dp)
                    start.linkTo(categoryTag.end, 10.dp)
                },
                fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.roboto_regular)),
                fontSize = 12.sp,
                color = Color.Black
            )

            Image(
                modifier = Modifier
                    .size(10.dp)
                    .constrainAs(whiteDotImage) {
                        bottom.linkTo(authorName.bottom)
                        top.linkTo(authorName.top)
                        start.linkTo(authorName.end, 10.dp)
                    },
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = com.tsciences.dailycaller.android.R.drawable.ic_fiber_manual_record),
                contentDescription = "white dot",
                colorFilter = ColorFilter.tint(Color.Black)

            )

            getTime(
                news.pubDate, AppConstants.DATE_FORMAT_TIME_STAMP
            )?.let {
                Text(
                    text = it,
                    modifier = Modifier.constrainAs(time) {
                        top.linkTo(title.bottom, 5.dp)
                        start.linkTo(whiteDotImage.end, 10.dp)
                    },
                    fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.roboto_regular)),
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNewsItemTab1(
    news: Item,
    onNewsItemClick: (Item) -> Unit,
) {
    Surface(onClick = { onNewsItemClick(news) }) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f)
                .background(Color.White)
                .padding(SpacingXSmall)
        ) {
            val (newsImage, categoryTag, title, authorName, whiteDotImage, time, authorImage, patriotImage) = createRefs()
            AsyncImage(model = news.imageLargeUrl
                ?: news.image?.loc?.text,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .constrainAs(newsImage) {},placeholder = painterResource(id = R.drawable.default_image))

            if (news.premiumContent == true) {
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                        .constrainAs(patriotImage) {
                            start.linkTo(newsImage.start, 10.dp)
                            bottom.linkTo(newsImage.bottom, 10.dp)
                        },
                    painter = painterResource(id = com.tsciences.dailycaller.android.R.drawable.patriots),
                    contentDescription = "Section Gradient",
                )
            }

            Text(
                text = stripHtml(news.title),
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(newsImage.bottom, 10.dp)
                    start.linkTo(parent.start, 10.dp)
                },
                maxLines = 2,
                fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.sofia_pro_semibold)),
                fontSize = 18.sp,
                color = Color.Black
            )

            Text(text = news.categoryTag.toString(),

                modifier = Modifier
                    .background(
                        color = getCategoryColor(news.categoryTag.toString()),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .constrainAs(categoryTag) {
                        top.linkTo(newsImage.bottom)
                        bottom.linkTo(newsImage.bottom)
                        start.linkTo(parent.start, 10.dp)
                    }
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White)
            Text(
                text = news.authorName.toString(),

                modifier = Modifier.constrainAs(authorName) {
                    top.linkTo(title.bottom, 5.dp)
                    start.linkTo(parent.start, 10.dp)
                },
                fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.roboto_regular)),
                fontSize = 14.sp,
                color = Color.Black
            )

            Image(
                modifier = Modifier
                    .size(10.dp)
                    .constrainAs(whiteDotImage) {
                        bottom.linkTo(authorName.bottom)
                        top.linkTo(authorName.top)
                        start.linkTo(authorName.end, 10.dp)
                    },
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = com.tsciences.dailycaller.android.R.drawable.ic_fiber_manual_record),
                contentDescription = "white dot",
                colorFilter = ColorFilter.tint(Color.Black)

            )

            getTime(
                news.pubDate, AppConstants.DATE_FORMAT_TIME_STAMP
            )?.let {
                Text(
                    text = it,
                    modifier = Modifier.constrainAs(time) {
                        top.linkTo(title.bottom, 5.dp)
                        start.linkTo(whiteDotImage.end, 10.dp)
                    },
                    fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.roboto_regular)),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNewsItemTab2(
    news: Item,
    onNewsItemClick: (Item) -> Unit,
) {
    Surface(onClick = { onNewsItemClick(news) }) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f)
                .padding(SpacingXSmall)
                .background(Color.White)
        ) {

            val (collapsibleNewsImage, collapsibleNewsTransparentView, categoryTag, title, authorName, whiteDotImage, time, authorImage, patriotImage) = createRefs()
            AsyncImage(model = news.imageLargeUrl
                ?: news.image?.loc?.text,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .constrainAs(collapsibleNewsImage) {},placeholder = painterResource(id = R.drawable.default_image)
            )

            if (news.premiumContent) {
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                        .constrainAs(patriotImage) {
                            end.linkTo(parent.end, 10.dp)
                            top.linkTo(parent.top, 10.dp)
                        },
                    painter = painterResource(id = com.tsciences.dailycaller.android.R.drawable.patriots),
                    contentDescription = "Section Gradient",
                )
            }

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .constrainAs(collapsibleNewsTransparentView) {
                        bottom.linkTo(collapsibleNewsImage.bottom)
                    }
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 8.dp, bottomStart = 8.dp
                        )
                    ),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = com.tsciences.dailycaller.android.R.drawable.section_gradient),
                alignment = Alignment.BottomCenter,
                contentDescription = "Section Gradient",

                )

            Text(text = news.categoryTag.toString(),

                modifier = Modifier
                    .background(
                        color = getCategoryColor(news.categoryTag.toString()),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .constrainAs(categoryTag) {
                        top.linkTo(collapsibleNewsImage.top, 10.dp)
                        start.linkTo(
                            collapsibleNewsTransparentView.start, 10.dp
                        )
                    }
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.roboto_medium)),
                color = Color.White)
            Text(
                text = stripHtml(news.title),
                modifier = Modifier.constrainAs(title) {
                    bottom.linkTo(authorName.top, 5.dp)
                    start.linkTo(
                        collapsibleNewsTransparentView.start,
                        10.dp
                    )
                },
                maxLines = 2,
                fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.sofia_pro_semibold)),
                fontSize = 18.sp,
                color = Color.White
            )

            Text(
                text = news.authorName.toString(),

                modifier = Modifier.constrainAs(authorName) {
                    bottom.linkTo(parent.bottom, 20.dp)
                    start.linkTo(
                        collapsibleNewsTransparentView.start,
                        10.dp
                    )
                },
                fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.roboto_regular)),
                fontSize = 14.sp,
                color = Color.White
            )

            Image(
                modifier = Modifier
                    .size(10.dp)
                    .constrainAs(whiteDotImage) {
                        start.linkTo(authorName.end, 10.dp)
                        bottom.linkTo(authorName.bottom)
                        top.linkTo(authorName.top)
                    },
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = com.tsciences.dailycaller.android.R.drawable.ic_fiber_manual_record),
                contentDescription = "white dot",

                )

            getTime(
                news.pubDate,
                AppConstants.DATE_FORMAT_TIME_STAMP
            )?.let {
                Text(
                    text = it,
                    modifier = Modifier.constrainAs(time) {
                        start.linkTo(whiteDotImage.end, 10.dp)
                        bottom.linkTo(parent.bottom, 20.dp)
                    },
                    fontFamily = FontFamily(Font(com.tsciences.dailycaller.android.R.font.roboto_regular)),
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
            AsyncImage(model = news.authorImage,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(30.dp)
                    .constrainAs(authorImage) {
                        bottom.linkTo(time.bottom)
                        top.linkTo(time.top)
                        end.linkTo(parent.end, 10.dp)
                    }
                    .clip(MaterialTheme.shapes.extraSmall))
        }
    }
}

@Composable
fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .border(
                width = BorderWidthDefault,
                color = Color.Black,
                shape = MaterialTheme.shapes.large
            )
            .then(modifier)
    ) {
        Icon(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.close_button)),
            imageVector = ImageVector.vectorResource(R.drawable.ic_close),
            contentDescription = stringResource(R.string.content_desc_close),
            tint = Color.White
        )
    }
}

@Composable
fun ShareButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .border(
                width = BorderWidthDefault,
                color = Color.Black,
                shape = MaterialTheme.shapes.large
            )
            .then(modifier)
    ) {
        Icon(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.share_button)),
            painter = painterResource(id = R.drawable.share),
            contentDescription = stringResource(R.string.content_desc_close),
            tint = Color.White
        )
    }
}




