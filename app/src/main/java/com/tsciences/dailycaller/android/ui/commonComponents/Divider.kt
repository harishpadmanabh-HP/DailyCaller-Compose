package com.tsciences.dailycaller.android.ui.commonComponents

import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.tsciences.dailycaller.android.core.theme.gray

@Composable
fun DailyCallerDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = gray,
) {
    Divider(
        modifier = modifier, thickness = thickness, color = color
    )
}