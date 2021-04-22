package com.example.bookapiassignment

import retrofit2.Call
import retrofit2.http.*

interface BookService {
    @GET ( "volumes?key=AIzaSyCOsLNWVpQPaYGRmpgusFDPyD-LhAggvbg&maxResults=40&printType=books")

    fun bookSearchRequest(@Query("q" , encoded = true) searchTerm:String):Call<Book>
}