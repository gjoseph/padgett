package net.incongru.padgett

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *
 */
@EqualsAndHashCode
@ToString
class MessageDetails {
    /** channel is null for private messages */
    String channel
    String text
    UserDetails user
}
