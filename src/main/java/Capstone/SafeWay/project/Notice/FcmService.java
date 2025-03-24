package Capstone.SafeWay.project.Notice;

import Capstone.SafeWay.project.Notice.Dto.FcmSendRequestDto;
import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository; // FCM 토큰 조회용

    private final String FCM_SERVER_KEY = "YOUR_SERVER_KEY";

    public void sendPushAndSaveNotice(FcmSendRequestDto dto) throws IOException {

        // 1. 알림 저장
        NoticeEntity notice = NoticeEntity.builder()
                .userId(dto.getUserId())
                .category(dto.getCategory())
                .content(dto.getContent())
                .build();

        noticeRepository.save(notice);


        // 2. 사용자 FCM 토큰 가져오기
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        String token = user.getFcmToken();
        if (token == null) return;

        // 3. FCM 전송
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> body = new HashMap<>();
        body.put("to", token);

        Map<String, String> notification = new HashMap<>();
        notification.put("title", dto.getCategory());
        notification.put("body", dto.getContent());
        body.put("notification", notification);

        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .addHeader("Authorization", "key=" + FCM_SERVER_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        objectMapper.writeValueAsString(body),
                        MediaType.get("application/json")
                ))
                .build();

        client.newCall(request).execute();
    }
}
