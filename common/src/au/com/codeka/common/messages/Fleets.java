// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./messages.proto
package au.com.codeka.common.messages;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Label.REPEATED;

public final class Fleets extends Message {

  public static final List<Fleet> DEFAULT_FLEETS = Collections.emptyList();

  @ProtoField(tag = 1, label = REPEATED)
  public final List<Fleet> fleets;

  public Fleets(List<Fleet> fleets) {
    this.fleets = immutableCopyOf(fleets);
  }

  private Fleets(Builder builder) {
    this(builder.fleets);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Fleets)) return false;
    return equals(fleets, ((Fleets) other).fleets);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = fleets != null ? fleets.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<Fleets> {

    public List<Fleet> fleets;

    public Builder() {
    }

    public Builder(Fleets message) {
      super(message);
      if (message == null) return;
      this.fleets = copyOf(message.fleets);
    }

    public Builder fleets(List<Fleet> fleets) {
      this.fleets = checkForNulls(fleets);
      return this;
    }

    @Override
    public Fleets build() {
      return new Fleets(this);
    }
  }
}
