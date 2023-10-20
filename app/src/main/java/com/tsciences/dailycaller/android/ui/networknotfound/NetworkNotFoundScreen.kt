package com.tsciences.dailycaller.android.ui.networknotfound

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.theme.DailyCallerTheme
import com.tsciences.dailycaller.android.core.theme.LightGray
import com.tsciences.dailycaller.android.core.theme.gray
import com.tsciences.dailycaller.android.ui.commonComponents.DailyCallerScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkNotFoundScreen() {
    DailyCallerScaffold(containerColor = gray) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxHeight()
                .background(LightGray)
        ) {
            val (logo, pageNotFound, pageNotFoundBackground) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.logo_tablet),
                contentDescription = "options",
                modifier = Modifier
                    .wrapContentWidth()
                    .height(dimensionResource(id = R.dimen.logo_height))
                    .constrainAs(logo) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(pageNotFound.top)
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.page_not_found),
                contentDescription = "options",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .constrainAs(pageNotFound) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(pageNotFoundBackground.top)
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.page_not_found_bg),
                contentDescription = "options",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .constrainAs(pageNotFoundBackground) {
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewReceiveQrScreen() {
    DailyCallerTheme {
        NetworkNotFoundScreen(
        )
    }
}