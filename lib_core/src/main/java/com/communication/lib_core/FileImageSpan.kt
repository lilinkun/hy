package com.communication.lib_core

import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

class FileImageSpan (drawable: Drawable, val fileName: String) : ImageSpan(drawable, ALIGN_BOTTOM)