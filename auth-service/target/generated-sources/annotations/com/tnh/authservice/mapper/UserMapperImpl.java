package com.tnh.authservice.mapper;

import com.tnh.authservice.domain.User;
import com.tnh.authservice.dto.UserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-14T17:20:01+0700",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.4 (Debian)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO mapToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUsername( user.getUsername() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setActivationKey( user.getActivationKey() );

        userDTO.setId( convertIdToString(user.getId()) );

        return userDTO;
    }

    @Override
    public UserDTO mapToUserDTOWithoutActivationKey(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUsername( user.getUsername() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setEmail( user.getEmail() );

        userDTO.setId( convertIdToString(user.getId()) );

        return userDTO;
    }
}
