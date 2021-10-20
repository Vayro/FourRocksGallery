package com.vayrotech.fourrocksgallery;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Classifier {
   private Interpreter interpreter;
   private List<String> LabelList;
   private int INPUT_SIZE;
   private int PS =3;
   private int imgAvg=0;
  private float imgSTD=255.0f;
   private float MAX_RESULTS=3;
   private  float THRESHOLD=0.4F;
   Classifier(AssetManager assetManager, String modelPath, String labelPath, int inputSize) throws IOException{
       INPUT_SIZE=inputSize;
       Interpreter.Options options = new Interpreter.Options();
       options.setNumThreads(5);
       options.setUseNNAPI(true);
       interpreter = new Interpreter( loadModelFile(assetManager,modelPath), options );
       LabelList=loadLabelList(assetManager,labelPath);
   }

   class Recognition{
       String id=" ";
       String title=" ";
       float confidence= 0F;

      public Recognition(String i, String title, float confidence){
          id=i;
          this.title= title;
           this.confidence=confidence;


       }
       @NonNull
       @Override
       public String toString(){

          return  title + ", Confidence=" + confidence *100 + "%";
       }

   }



   private MappedByteBuffer loadModelFile(AssetManager assetManager,String MODEL_FILE) throws IOException {
       AssetFileDescriptor fileDescriptor = assetManager.openFd(MODEL_FILE);
       FileInputStream inputStream =  new FileInputStream(fileDescriptor.getFileDescriptor());
       FileChannel fileChannel = inputStream.getChannel();
       long startOffset = fileDescriptor.getStartOffset();
       long declaredLength = fileDescriptor.getDeclaredLength();
       return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
   }



   private List<String> loadLabelList(AssetManager assetManager,String labelPath) throws IOException{
       List<String>labelList=new ArrayList<>();
       BufferedReader reader= new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
       String line;
       while((line=reader.readLine())!=null){
           labelList.add(line);
       }
       reader.close();
       return labelList;
   }



   List <Recognition>recognizeImage(Bitmap bitmap ){
       Bitmap scaledBitmap=Bitmap.createScaledBitmap(bitmap,INPUT_SIZE, INPUT_SIZE, false);
       ByteBuffer byteBuffer = convertBitmapToByteBuffer(scaledBitmap);
       float[][] result=  new float[1][LabelList.size()];
       interpreter.run(byteBuffer, result);
       return getSortedResultFloat(result);
   }




   private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap){
       ByteBuffer byteBuffer;
       byteBuffer=ByteBuffer.allocateDirect(4*INPUT_SIZE*INPUT_SIZE*PS);


       byteBuffer.order(ByteOrder.nativeOrder());
       int[] intValues = new int[INPUT_SIZE*INPUT_SIZE];
       bitmap.getPixels(intValues, 0, bitmap.getWidth(),0, 0,bitmap.getWidth(),bitmap.getHeight());
       int pixel = 0;
       for( int i = 0;i <INPUT_SIZE;++i){
           for(int j=0;j<INPUT_SIZE;++j){
               final int val = intValues[pixel++];
               byteBuffer.putFloat((((val>>16)&0xFF)-imgAvg)/imgSTD);
               byteBuffer.putFloat((((val>>8)&0xFF)-imgAvg)/imgSTD);
              byteBuffer.putFloat((((val)&0xFF)-imgAvg)/imgSTD);

           }
       } return byteBuffer;

   }




   @SuppressLint("DefaultLocale")
   private List<Recognition> getSortedResultFloat(float[][] labelProbArray){
       PriorityQueue<Recognition> pq=
               new PriorityQueue<>(
                       (int) MAX_RESULTS,
                       (Ihs, rhs) -> Float.compare(rhs.confidence,Ihs.confidence));
       for( int i =0; i<LabelList.size();++i){
           float confidence = labelProbArray[0][i];
           if (confidence> THRESHOLD){
               LabelList.size();
               pq.add(new Recognition(""+i, LabelList.get(i),
                       confidence));
           }

       }
       final ArrayList<Recognition> recognitions = new ArrayList<>();
       int recognitionsSize= (int) Math.min(pq.size(),MAX_RESULTS);
       for(int i=0;i<recognitionsSize;++i) {
           recognitions.add(pq.poll());
       }
       return recognitions;

   }

}
