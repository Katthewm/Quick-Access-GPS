package com.example.quickaccessgps

object AddressSingleton {
    public val addresses = arrayListOf(
        Address("Home", "asdfjasldkfa", true),
        Address("Home 2", "asdkfjlasdkjfaslkdjfa", true)
    )

    public fun addAddress(address: Address) {
        addresses.add(address)
        sortAddresses()
    }

    private fun sortAddresses() {
        addresses.sortWith(Comparator { first, second ->
            if (first.isFavorite && !second.isFavorite) {
                return@Comparator -1
            } else if (!first.isFavorite && second.isFavorite) {
                return@Comparator 1
            } else {
                return@Comparator first.name.compareTo(second.name)
            }
        })
    }

    fun removeAddress(addressIndex: Int) {
        addresses.removeAt(addressIndex)
    }
}