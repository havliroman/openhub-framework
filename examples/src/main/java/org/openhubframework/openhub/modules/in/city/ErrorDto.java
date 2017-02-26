/*
 * Copyright (c) 2016 BSC Praha, spol. s r.o.
 */

package org.openhubframework.openhub.modules.in.city;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Simple error DTO for human readable exception presentation.
 *
 * @author <a href="mailto:roman.havlicek@bsc-ideas.com">Roman Havlicek</a>
 */
public class ErrorDto {

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }

        if (this.getClass() != o.getClass()) {
            return false;
        }

        ErrorDto errorDto = (ErrorDto) o;

        return new EqualsBuilder()
                .append(this.errorMessage, errorDto.errorMessage)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(errorMessage)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("errorMessage", errorMessage)
                .toString();
    }
}
