package configs;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:properties/user.properties"})
public interface UserDataConfig extends Config {
    @Key("id")
    String setId ();
    @Key("username")
    String setUsername ();
    @Key("firstName")
    String setFirstName ();
    @Key("lastName")
    String setLastName ();
    @Key("email")
    String setEmail ();
    @Key("phone")
    String setPhone ();
    @Key("password")
    String setPassword ();
    @Key("userStatus")
    String setUserStatus ();

}
