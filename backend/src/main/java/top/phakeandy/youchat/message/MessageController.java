package top.phakeandy.youchat.message;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Tag(name = "Message")
public class MessageController {

  private final MessageService messageService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void saveMessage(@RequestBody MessageRequest message) {
    messageService.saveMessage(message);
  }
}
