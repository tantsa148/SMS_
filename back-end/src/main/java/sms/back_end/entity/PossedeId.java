package sms.back_end.entity;

import java.io.Serializable;
import java.util.Objects;

public class PossedeId implements Serializable {
    private Long userId;
    private Long numeroId;

    public PossedeId() {}

    public PossedeId(Long userId, Long numeroId) {
        this.userId = userId;
        this.numeroId = numeroId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        PossedeId that = (PossedeId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(numeroId, that.numeroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, numeroId);
    }
}
