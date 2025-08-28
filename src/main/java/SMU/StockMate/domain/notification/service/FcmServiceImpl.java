package SMU.StockMate.domain.notification.service;

import SMU.StockMate.domain.notification.dto.FcmMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/stockmate-42ee8/messages:send";
    private final ObjectMapper objectMapper;

    @Override
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        try {
            String message = makeMessage(targetToken, title, body);
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json; UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(message))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("FCM 전송 실패", e);
        }
    }

    private String makeMessage(String targetToken, String title, String body)
            throws JsonParseException, JsonProcessingException {

        FcmMessage fcmMessage = FcmMessage.builder()
                .validateOnly(false)
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .build())
                        .build())
                .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
