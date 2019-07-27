package com.mycompany.ingester;

import java.io.Serializable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

public class InsuranceInformation implements Serializable {
	public static final class Builder {
		private String companyName;
		private String insuredName;
		private String userId;
		private int version;

		private Builder() {
		}

		public InsuranceInformation build() {
			return new InsuranceInformation(this);
		}

		public Builder withCompanyName(String val) {
			companyName = val;
			return this;
		}

		public Builder withInsuredName(String val) {
			insuredName = val;
			return this;
		}

		public Builder withUserId(String val) {
			userId = val;
			return this;
		}

		public Builder withVersion(int val) {
			version = val;
			return this;
		}
	}

	private static final long serialVersionUID = 5715276071789319010L;

	private final String companyName;
	private final String insuredName;
	private final String userId;
	private final int version;

	private InsuranceInformation(Builder builder) {
		companyName = builder.companyName;
		insuredName = builder.insuredName;
		userId = builder.userId;
		version = builder.version;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static Builder newBuilder(InsuranceInformation copy) {
		Builder builder = new Builder();
		builder.companyName = copy.getCompanyName();
		builder.insuredName = copy.getInsuredName();
		builder.userId = copy.getUserId();
		builder.version = copy.getVersion();
		return builder;
	}

	public String getNameFormattedByLastAndFirst() {
		if (Strings.isNullOrEmpty(insuredName)) {
			return "";
		}
		String[] tokenParts = insuredName.split("\\s+", 2);
		return String.format(
			"%s %s",
			MoreObjects.firstNonNull(tokenParts[1], ""),
			MoreObjects.firstNonNull(tokenParts[0], "")
		);
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public String getUserId() {
		return userId;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("companyName", companyName)
			.add("insuredName", insuredName)
			.add("userId", userId)
			.add("version", version)
			.toString();
	}
}
