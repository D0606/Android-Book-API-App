package com.example.bookapiassignment

import retrofit2.Call
import retrofit2.http.*

interface BookService {
    //Basic API call with key
    @GET ( "volumes?key=AIzaSyCOsLNWVpQPaYGRmpgusFDPyD-LhAggvbg&maxResults=40&printType=books")

    //Identify 'q' as query key insert, can be placed anywhere in query along with the passed searchTerm
    fun bookSearchRequest(@Query("q" , encoded = true) searchTerm:String):Call<Book>
}