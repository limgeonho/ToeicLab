package toeicLab.toeicLab.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisionService {

    public void readText(String fileName, StringBuilder loadText){
        try {
//            ClassPathResource resource = new ClassPathResource("/questionPhoto/" + fileName + ".jpg");
            String resource = System.getProperty("java.io.tmpdir") + fileName + ".jpg";
//            String imageFilePath = resource.getFile().getPath();

            List<AnnotateImageRequest> requests = new ArrayList<>();
            ByteString imgBytes = ByteString.readFrom(new FileInputStream(resource));
//            ByteString imgBytes = ByteString.readFrom(new FileInputStream(imageFilePath));

            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        System.out.printf("Error: %s\n", res.getError().getMessage());
                        return;
                    }

                    System.out.println("Text : ");
                    System.out.println(res.getTextAnnotationsList().get(0).getDescription());
                    loadText.append(res.getTextAnnotationsList().get(0).getDescription());

                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
