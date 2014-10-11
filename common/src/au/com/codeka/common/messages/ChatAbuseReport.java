// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./messages.proto
package au.com.codeka.common.messages;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.INT32;

/**
 *
 * When you report a message for abuse, this is what you post to the server.
 */
public final class ChatAbuseReport extends Message {

  public static final Integer DEFAULT_CHAT_MSG_ID = 0;

  @ProtoField(tag = 1, type = INT32)
  public final Integer chat_msg_id;

  public ChatAbuseReport(Integer chat_msg_id) {
    this.chat_msg_id = chat_msg_id;
  }

  private ChatAbuseReport(Builder builder) {
    this(builder.chat_msg_id);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ChatAbuseReport)) return false;
    return equals(chat_msg_id, ((ChatAbuseReport) other).chat_msg_id);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = chat_msg_id != null ? chat_msg_id.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<ChatAbuseReport> {

    public Integer chat_msg_id;

    public Builder() {
    }

    public Builder(ChatAbuseReport message) {
      super(message);
      if (message == null) return;
      this.chat_msg_id = message.chat_msg_id;
    }

    public Builder chat_msg_id(Integer chat_msg_id) {
      this.chat_msg_id = chat_msg_id;
      return this;
    }

    @Override
    public ChatAbuseReport build() {
      return new ChatAbuseReport(this);
    }
  }
}
