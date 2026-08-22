package com.mafiagame.freemium.model

enum class Team {
    VILLAGE,
    MAFIA,
    INDEPENDENT
}

enum class RoleId {
    // Free
    VILLAGER,
    MAFIA,
    DOCTOR,
    DETECTIVE,
    // Premium
    BODYGUARD,
    JESTER,
    SERIAL_KILLER,
    MAYOR,
    VIGILANTE,
    WITCH,
    CUPID,
    HUNTER,
    FOOL,
    ARSONIST
}

data class Role(
    val id: RoleId,
    val name: String,
    val team: Team,
    val description: String,
    val isPremium: Boolean = false,
    val nightAction: Boolean = false,
    val dayAction: Boolean = false
)

object RoleCatalog {
    val freeRoles = listOf(
        Role(RoleId.VILLAGER, "Villager", Team.VILLAGE, "No special power. Find the Mafia!", false),
        Role(RoleId.MAFIA, "Mafia", Team.MAFIA, "Kill one player each night. Stay hidden!", false, nightAction = true),
        Role(RoleId.DOCTOR, "Doctor", Team.VILLAGE, "Protect one player from death each night.", false, nightAction = true),
        Role(RoleId.DETECTIVE, "Detective", Team.VILLAGE, "Investigate one player each night to learn if Mafia.", false, nightAction = true)
    )

    val premiumRoles = listOf(
        Role(RoleId.BODYGUARD, "Bodyguard", Team.VILLAGE, "Protect one player. If attacked, dies instead and may kill the attacker.", true, nightAction = true),
        Role(RoleId.JESTER, "Jester", Team.INDEPENDENT, "Win if you are lynched during the day.", true),
        Role(RoleId.SERIAL_KILLER, "Serial Killer", Team.INDEPENDENT, "Kill one player each night. Win alone.", true, nightAction = true),
        Role(RoleId.MAYOR, "Mayor", Team.VILLAGE, "Your vote counts as two during the day.", true, dayAction = true),
        Role(RoleId.VIGILANTE, "Vigilante", Team.VILLAGE, "May shoot one player during the day (limited uses).", true, dayAction = true),
        Role(RoleId.WITCH, "Witch", Team.INDEPENDENT, "Has one poison and one save potion.", true, nightAction = true),
        Role(RoleId.CUPID, "Cupid", Team.VILLAGE, "Links two players in love on first night. They die together.", true, nightAction = true),
        Role(RoleId.HUNTER, "Hunter", Team.VILLAGE, "When you die, you may take one player with you.", true),
        Role(RoleId.FOOL, "Fool", Team.INDEPENDENT, "Win if you are lynched. Similar to Jester.", true),
        Role(RoleId.ARSONIST, "Arsonist", Team.INDEPENDENT, "Douse players at night, ignite to kill all doused.", true, nightAction = true)
    )

    fun allRoles() = freeRoles + premiumRoles

    fun getById(id: RoleId) = allRoles().find { it.id == id }
}