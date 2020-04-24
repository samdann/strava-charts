/*
 * Strava API v3
 * The [Swagger Playground](https://developers.strava.com/playground) is the easiest way to familiarize yourself with the Strava API by submitting HTTP requests and observing the responses before you write any client code. It will show what a response will look like with different endpoints depending on the authorization scope you receive from your athletes. To use the Playground, go to https://www.strava.com/settings/api and change your “Authorization Callback Domain” to developers.strava.com. Please note, we only support Swagger 2.0. There is a known issue where you can only select one scope at a time. For more information, please check the section “client code” at https://developers.strava.com/docs.
 *
 * OpenAPI spec version: 3.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.MetaAthlete;
import java.io.IOException;
import org.threeten.bp.OffsetDateTime;

/**
 * SummaryAthlete
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-03-09T01:25:47.060+01:00")
public class SummaryAthlete {
  @SerializedName("id")
  private Integer id = null;

  @SerializedName("resource_state")
  private Integer resourceState = null;

  @SerializedName("firstname")
  private String firstname = null;

  @SerializedName("lastname")
  private String lastname = null;

  @SerializedName("profile_medium")
  private String profileMedium = null;

  @SerializedName("profile")
  private String profile = null;

  @SerializedName("city")
  private String city = null;

  @SerializedName("state")
  private String state = null;

  @SerializedName("country")
  private String country = null;

  /**
   * The athlete&#39;s sex.
   */
  @JsonAdapter(SexEnum.Adapter.class)
  public enum SexEnum {
    M("M"),
    
    F("F");

    private String value;

    SexEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static SexEnum fromValue(String text) {
      for (SexEnum b : SexEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<SexEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final SexEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public SexEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return SexEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("sex")
  private SexEnum sex = null;

  @SerializedName("premium")
  private Boolean premium = null;

  @SerializedName("summit")
  private Boolean summit = null;

  @SerializedName("created_at")
  private OffsetDateTime createdAt = null;

  @SerializedName("updated_at")
  private OffsetDateTime updatedAt = null;

  public SummaryAthlete id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * The unique identifier of the athlete
   * @return id
  **/
  @ApiModelProperty(value = "The unique identifier of the athlete")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public SummaryAthlete resourceState(Integer resourceState) {
    this.resourceState = resourceState;
    return this;
  }

   /**
   * Resource state, indicates level of detail. Possible values: 1 -&gt; \&quot;meta\&quot;, 2 -&gt; \&quot;summary\&quot;, 3 -&gt; \&quot;detail\&quot;
   * @return resourceState
  **/
  @ApiModelProperty(value = "Resource state, indicates level of detail. Possible values: 1 -> \"meta\", 2 -> \"summary\", 3 -> \"detail\"")
  public Integer getResourceState() {
    return resourceState;
  }

  public void setResourceState(Integer resourceState) {
    this.resourceState = resourceState;
  }

  public SummaryAthlete firstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

   /**
   * The athlete&#39;s first name.
   * @return firstname
  **/
  @ApiModelProperty(value = "The athlete's first name.")
  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public SummaryAthlete lastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

   /**
   * The athlete&#39;s last name.
   * @return lastname
  **/
  @ApiModelProperty(value = "The athlete's last name.")
  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public SummaryAthlete profileMedium(String profileMedium) {
    this.profileMedium = profileMedium;
    return this;
  }

   /**
   * URL to a 62x62 pixel profile picture.
   * @return profileMedium
  **/
  @ApiModelProperty(value = "URL to a 62x62 pixel profile picture.")
  public String getProfileMedium() {
    return profileMedium;
  }

  public void setProfileMedium(String profileMedium) {
    this.profileMedium = profileMedium;
  }

  public SummaryAthlete profile(String profile) {
    this.profile = profile;
    return this;
  }

   /**
   * URL to a 124x124 pixel profile picture.
   * @return profile
  **/
  @ApiModelProperty(value = "URL to a 124x124 pixel profile picture.")
  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public SummaryAthlete city(String city) {
    this.city = city;
    return this;
  }

   /**
   * The athlete&#39;s city.
   * @return city
  **/
  @ApiModelProperty(value = "The athlete's city.")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public SummaryAthlete state(String state) {
    this.state = state;
    return this;
  }

   /**
   * The athlete&#39;s state or geographical region.
   * @return state
  **/
  @ApiModelProperty(value = "The athlete's state or geographical region.")
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public SummaryAthlete country(String country) {
    this.country = country;
    return this;
  }

   /**
   * The athlete&#39;s country.
   * @return country
  **/
  @ApiModelProperty(value = "The athlete's country.")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public SummaryAthlete sex(SexEnum sex) {
    this.sex = sex;
    return this;
  }

   /**
   * The athlete&#39;s sex.
   * @return sex
  **/
  @ApiModelProperty(value = "The athlete's sex.")
  public SexEnum getSex() {
    return sex;
  }

  public void setSex(SexEnum sex) {
    this.sex = sex;
  }

  public SummaryAthlete premium(Boolean premium) {
    this.premium = premium;
    return this;
  }

   /**
   * Deprecated.  Use summit field instead. Whether the athlete has any Summit subscription.
   * @return premium
  **/
  @ApiModelProperty(value = "Deprecated.  Use summit field instead. Whether the athlete has any Summit subscription.")
  public Boolean isPremium() {
    return premium;
  }

  public void setPremium(Boolean premium) {
    this.premium = premium;
  }

  public SummaryAthlete summit(Boolean summit) {
    this.summit = summit;
    return this;
  }

   /**
   * Whether the athlete has any Summit subscription.
   * @return summit
  **/
  @ApiModelProperty(value = "Whether the athlete has any Summit subscription.")
  public Boolean isSummit() {
    return summit;
  }

  public void setSummit(Boolean summit) {
    this.summit = summit;
  }

  public SummaryAthlete createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

   /**
   * The time at which the athlete was created.
   * @return createdAt
  **/
  @ApiModelProperty(value = "The time at which the athlete was created.")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public SummaryAthlete updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

   /**
   * The time at which the athlete was last updated.
   * @return updatedAt
  **/
  @ApiModelProperty(value = "The time at which the athlete was last updated.")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SummaryAthlete summaryAthlete = (SummaryAthlete) o;
    return Objects.equals(this.id, summaryAthlete.id) &&
        Objects.equals(this.resourceState, summaryAthlete.resourceState) &&
        Objects.equals(this.firstname, summaryAthlete.firstname) &&
        Objects.equals(this.lastname, summaryAthlete.lastname) &&
        Objects.equals(this.profileMedium, summaryAthlete.profileMedium) &&
        Objects.equals(this.profile, summaryAthlete.profile) &&
        Objects.equals(this.city, summaryAthlete.city) &&
        Objects.equals(this.state, summaryAthlete.state) &&
        Objects.equals(this.country, summaryAthlete.country) &&
        Objects.equals(this.sex, summaryAthlete.sex) &&
        Objects.equals(this.premium, summaryAthlete.premium) &&
        Objects.equals(this.summit, summaryAthlete.summit) &&
        Objects.equals(this.createdAt, summaryAthlete.createdAt) &&
        Objects.equals(this.updatedAt, summaryAthlete.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, resourceState, firstname, lastname, profileMedium, profile, city, state, country, sex, premium, summit, createdAt, updatedAt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SummaryAthlete {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    resourceState: ").append(toIndentedString(resourceState)).append("\n");
    sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
    sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
    sb.append("    profileMedium: ").append(toIndentedString(profileMedium)).append("\n");
    sb.append("    profile: ").append(toIndentedString(profile)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    sex: ").append(toIndentedString(sex)).append("\n");
    sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
    sb.append("    summit: ").append(toIndentedString(summit)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

