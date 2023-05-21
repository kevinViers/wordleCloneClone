import com.parse.ParseUser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddFriendTest {

    private lateinit var addFriend: AddFriend

    @Before
    fun setUp() {
        addFriend = AddFriend()
    }

    @Test
    fun testMapChanged() {
        val user1 = ParseUser().apply { objectId = "1"; username = "user1" }
        val user2 = ParseUser().apply { objectId = "2"; username = "user2" }
        val user3 = ParseUser().apply { objectId = "3"; username = "user3" }

        val inputMap = mapOf("1" to user1, "2" to user2, "3" to user3)
        val outputList = arrayListOf<ParseUser>()

        addFriend.mapChanged(inputMap, outputList)

        assertEquals(3, outputList.size)
        assertEquals(user1, outputList[0])
        assertEquals(user2, outputList[1])
        assertEquals(user3, outputList[2])
    }
}
