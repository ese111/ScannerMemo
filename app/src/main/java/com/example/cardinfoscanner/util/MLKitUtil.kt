package com.example.cardinfoscanner.util

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

fun recognizeText(image: InputImage): Task<Text> {
    val koreanRecognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    Log.i("흥수", "rr")
    return koreanRecognizer.process(image)
}

private fun processTextBlock(result: Text) {
    // [START mlkit_process_text_block]
    val resultText = result.text
    Log.i("흥수2", resultText)
    for (block in result.textBlocks) {
        val blockText = block.text
        val blockCornerPoints = block.cornerPoints
        val blockFrame = block.boundingBox
        Log.i("흥수3", blockText)
        for (line in block.lines) {
            val lineText = line.text
            val lineCornerPoints = line.cornerPoints
            val lineFrame = line.boundingBox
            Log.i("흥수4", lineText)
            for (element in line.elements) {
                val elementText = element.text
                val elementCornerPoints = element.cornerPoints
                val elementFrame = element.boundingBox
                Log.i("흥수5", elementText)
            }
        }
    }
}