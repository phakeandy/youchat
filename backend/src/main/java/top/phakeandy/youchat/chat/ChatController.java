package top.phakeandy.youchat.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import top.phakeandy.youchat.chat.request.CreateChatRequest;
import top.phakeandy.youchat.chat.request.UpdateChatRequest;

@RestController
@RequestMapping("/api/v1/chats")
@Tag(name = "Chat", description = "聊天会话相关接口")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  @PostMapping
  @Operation(summary = "创建群聊", description = "创建一个新的群聊会话")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "群聊创建成功",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ChatResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "请求参数错误",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class))),
      })
  public ResponseEntity<ChatResponse> createChat(@RequestBody CreateChatRequest request) {
    ChatResponse response = chatService.createChat(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  @Operation(summary = "获取群聊列表", description = "分页获取所有群聊列表")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "成功获取群聊列表",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Page.class))),
      })
  public ResponseEntity<Page<ChatResponse>> getChats(Pageable pageable) {
    Page<ChatResponse> response = chatService.getChats(pageable);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{chatId}")
  @Operation(summary = "获取群聊详情", description = "根据群聊ID获取群聊详细信息")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "成功获取群聊详情",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ChatResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "群聊不存在",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class))),
      })
  public ResponseEntity<ChatResponse> getChat(
      @Parameter(description = "群聊ID", required = true, example = "1") @PathVariable Long chatId) {
    ChatResponse response = chatService.getChat(chatId);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{chatId}")
  @Operation(summary = "更新群聊信息", description = "更新指定群聊的信息")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "群聊更新成功",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ChatResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "请求参数错误",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(
            responseCode = "404",
            description = "群聊不存在",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class))),
      })
  public ResponseEntity<ChatResponse> updateChat(
      @Parameter(description = "群聊ID", required = true, example = "1") @PathVariable Long chatId,
      @RequestBody UpdateChatRequest request) {
    ChatResponse response = chatService.updateChat(chatId, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{chatId}")
  @Operation(summary = "删除群聊", description = "删除指定的群聊")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "群聊删除成功"),
        @ApiResponse(
            responseCode = "404",
            description = "群聊不存在",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class))),
      })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteChat(
      @Parameter(description = "群聊ID", required = true, example = "1") @PathVariable Long chatId) {
    chatService.deleteChat(chatId);
  }
}
