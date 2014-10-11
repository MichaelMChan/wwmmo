// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./messages.proto
package au.com.codeka.common.messages;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Label.REPEATED;

public final class CombatReports extends Message {

  public static final List<CombatReport> DEFAULT_REPORTS = Collections.emptyList();

  @ProtoField(tag = 1, label = REPEATED)
  public final List<CombatReport> reports;

  public CombatReports(List<CombatReport> reports) {
    this.reports = immutableCopyOf(reports);
  }

  private CombatReports(Builder builder) {
    this(builder.reports);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CombatReports)) return false;
    return equals(reports, ((CombatReports) other).reports);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = reports != null ? reports.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<CombatReports> {

    public List<CombatReport> reports;

    public Builder() {
    }

    public Builder(CombatReports message) {
      super(message);
      if (message == null) return;
      this.reports = copyOf(message.reports);
    }

    public Builder reports(List<CombatReport> reports) {
      this.reports = checkForNulls(reports);
      return this;
    }

    @Override
    public CombatReports build() {
      return new CombatReports(this);
    }
  }
}
