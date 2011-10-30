package net.incongru.padgett

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *
 */
@EqualsAndHashCode
@ToString
class UserDetails {
    String nickname, login, hostname
    boolean op, voice
}
