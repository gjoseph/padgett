package net.incongru.padgett

import groovy.transform.EqualsAndHashCode

/**
 *
 */
@EqualsAndHashCode
class MessageDetails {
    /** channel is null for private messages */
    String channel
    String text
    UserDetails user
}
