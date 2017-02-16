package info.juanmendez.mock.realm.factories;

import info.juanmendez.mock.realm.dependencies.Compare;
import info.juanmendez.mock.realm.dependencies.RealmStorage;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by @juanmendezinfo on 2/15/2017.
 */
public class QueryFactory {

    //collections queried keyed by immediate class

    public static RealmQuery create(Class clazz ){

        HashMap<Class, ArrayList<RealmObject>> realmMap = RealmStorage.getRealmMap();
        HashMap<Class, ArrayList<RealmObject>> queryMap = RealmStorage.getQueryMap();
        queryMap.put(clazz, realmMap.get(clazz) );

        RealmQuery realmQuery = mock(RealmQuery.class);
        when( realmQuery.toString() ).thenReturn( "Realm:" + clazz.getName() );
        Whitebox.setInternalState( realmQuery, "clazz", clazz);

        when( realmQuery.findAll() ).thenAnswer(invocationOnMock ->{
            return ResultsFactory.create( clazz );
        });

        when( realmQuery.findFirst()).thenAnswer(invocationOnMock -> {
            ArrayList<RealmObject> realResults = queryMap.get(clazz);
            return realResults.get(0);
        });

        //TODO whens will also be moved over another class
        when( realmQuery.lessThan( any(), anyInt() ) ).thenAnswer( createComparison( realmQuery, Compare.less ) );
        when( realmQuery.lessThan( anyString(), anyByte()) ).thenAnswer( createComparison( realmQuery, Compare.less ) );
        when( realmQuery.lessThan( anyString(), anyDouble() ) ).thenAnswer( createComparison( realmQuery, Compare.less ) );
        when( realmQuery.lessThan( anyString(), anyFloat() ) ).thenAnswer( createComparison( realmQuery, Compare.less ) );
        when( realmQuery.lessThan( anyString(), anyLong() ) ).thenAnswer( createComparison( realmQuery, Compare.less ) );
        when( realmQuery.lessThan( anyString(), any(Date.class) ) ).thenAnswer( createComparison( realmQuery, Compare.less ) );


        when( realmQuery.lessThanOrEqualTo( any(), anyInt() ) ).thenAnswer( createComparison( realmQuery, Compare.lessOrEqual ) );
        when( realmQuery.lessThanOrEqualTo( anyString(), anyByte()) ).thenAnswer( createComparison( realmQuery, Compare.lessOrEqual) );
        when( realmQuery.lessThanOrEqualTo( anyString(), anyDouble() ) ).thenAnswer( createComparison( realmQuery, Compare.lessOrEqual) );
        when( realmQuery.lessThanOrEqualTo( anyString(), anyFloat() ) ).thenAnswer( createComparison( realmQuery, Compare.lessOrEqual) );
        when( realmQuery.lessThanOrEqualTo( anyString(), anyLong() ) ).thenAnswer( createComparison( realmQuery, Compare.lessOrEqual) );
        when( realmQuery.lessThanOrEqualTo( anyString(), any(Date.class) ) ).thenAnswer( createComparison( realmQuery, Compare.lessOrEqual) );

        when( realmQuery.greaterThan( any(), anyInt() ) ).thenAnswer( createComparison( realmQuery, Compare.more) );
        when( realmQuery.greaterThan( anyString(), anyByte()) ).thenAnswer( createComparison( realmQuery, Compare.more) );
        when( realmQuery.greaterThan( anyString(), anyDouble() ) ).thenAnswer( createComparison( realmQuery, Compare.more) );
        when( realmQuery.greaterThan( anyString(), anyFloat() ) ).thenAnswer( createComparison( realmQuery, Compare.more) );
        when( realmQuery.greaterThan( anyString(), anyLong() ) ).thenAnswer( createComparison( realmQuery, Compare.more) );
        when( realmQuery.greaterThan( anyString(), any(Date.class) ) ).thenAnswer( createComparison( realmQuery, Compare.more) );

        when( realmQuery.greaterThanOrEqualTo( any(), anyInt() ) ).thenAnswer( createComparison( realmQuery, Compare.moreOrEqual) );
        when( realmQuery.greaterThanOrEqualTo( anyString(), anyByte()) ).thenAnswer( createComparison( realmQuery, Compare.moreOrEqual) );
        when( realmQuery.greaterThanOrEqualTo( anyString(), anyDouble() ) ).thenAnswer( createComparison( realmQuery, Compare.moreOrEqual) );
        when( realmQuery.greaterThanOrEqualTo( anyString(), anyFloat() ) ).thenAnswer( createComparison( realmQuery, Compare.moreOrEqual) );
        when( realmQuery.greaterThanOrEqualTo( anyString(), anyLong() ) ).thenAnswer( createComparison( realmQuery, Compare.moreOrEqual) );
        when( realmQuery.greaterThanOrEqualTo( anyString(), any(Date.class) ) ).thenAnswer( createComparison( realmQuery, Compare.moreOrEqual) );

        when( realmQuery.equalTo( anyString(), anyInt() ) ).thenAnswer( createComparison( realmQuery, Compare.equal ) );
        when( realmQuery.equalTo( anyString(), anyByte()) ).thenAnswer( createComparison( realmQuery, Compare.equal ) );
        when( realmQuery.equalTo( anyString(), anyDouble() ) ).thenAnswer( createComparison( realmQuery, Compare.equal ) );
        when( realmQuery.equalTo( anyString(), anyFloat() ) ).thenAnswer( createComparison( realmQuery, Compare.equal ) );
        when( realmQuery.equalTo( anyString(), anyLong() ) ).thenAnswer( createComparison( realmQuery, Compare.equal ) );
        when( realmQuery.equalTo( anyString(), anyString() ) ).thenAnswer( createComparison( realmQuery, Compare.equal ) );
        when( realmQuery.equalTo( anyString(), anyBoolean() ) ).thenAnswer( createComparison( realmQuery, Compare.equal ) );
        when( realmQuery.equalTo( anyString(), any(Date.class) ) ).thenAnswer( createComparison( realmQuery, Compare.equal ) );

        return realmQuery;
    }


    /**
     * This method filters using args.condition and updates queryMap<realmQuery.clazz, collection>
     * @param realmQuery queryMap.get( realmQuery.clazz ) gives key to get collection from queryMap
     * @param condition based on Compare.enums
     * @return the same args.realmQuery
     */
    private static Answer<RealmQuery> createComparison( RealmQuery realmQuery, String condition ){

        Class realmQueryClass = (Class) Whitebox.getInternalState( realmQuery, "clazz");

        return new Answer<RealmQuery>() {
            @Override
            public RealmQuery answer(InvocationOnMock invocationOnMock) throws Throwable {

                String type = (String) invocationOnMock.getArguments()[0];
                Object value = invocationOnMock.getArguments()[1];
                Class clazz = value.getClass();

                if( type.isEmpty() )
                    return realmQuery;

                ArrayList<RealmObject> queriedList = new ArrayList<>();
                ArrayList<RealmObject> searchList = new ArrayList<>();
                HashMap<Class, ArrayList<RealmObject>> queryMap = RealmStorage.getQueryMap();
                searchList = queryMap.get(realmQueryClass);

                for (RealmObject realmObject: searchList) {

                    //RunTimeErrorException if search field is not found in realmQueryClass
                    Object thisValue = Whitebox.getInternalState( realmObject, type );

                    if( thisValue != null ){

                        if( condition == Compare.equal ){

                            if( clazz == Date.class && ( ((Date)thisValue) ).compareTo( (Date)value ) == 0 ){
                                queriedList.add( realmObject);
                            }else if(value.equals(thisValue)){
                                queriedList.add( realmObject);
                            }
                        }
                        else
                        if( condition == Compare.less){

                            if( clazz == Date.class && ( ((Date)thisValue) ).compareTo( (Date)value ) < 0 ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Byte.class && ((byte)thisValue) < ((byte)value)){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Integer.class && ((int)thisValue) < ((int)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Double.class && ((double)thisValue) < ((double)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Long.class && ((long)thisValue) < ((long)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Float.class && ((float)thisValue) < ((float)value) ){
                                queriedList.add( realmObject);
                            }
                        }
                        else if( condition == Compare.lessOrEqual){

                            if( clazz == Date.class && ( ((Date)thisValue) ).compareTo( (Date)value ) <= 0 ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Byte.class && ((byte)thisValue) <= ((byte)value)){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Integer.class && ((int)thisValue) <= ((int)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Double.class && ((double)thisValue) <= ((double)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Long.class && ((long)thisValue) <= ((long)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Float.class && ((float)thisValue) <= ((float)value) ){
                                queriedList.add( realmObject);
                            }
                        }
                        else if( condition == Compare.more ){

                            if( clazz == Date.class && ( ((Date)thisValue) ).compareTo( (Date)value ) > 0 ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Byte.class && ((byte)thisValue) > ((byte)value)){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Integer.class && ((int)thisValue) > ((int)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Double.class && ((double)thisValue) > ((double)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Long.class && ((long)thisValue) > ((long)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Float.class && ((float)thisValue) > ((float)value) ){
                                queriedList.add( realmObject);
                            }
                        }
                        else if( condition == Compare.moreOrEqual ){

                            if( clazz == Date.class && ( ((Date)thisValue) ).compareTo( (Date)value ) >= 0 ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Byte.class && ((byte)thisValue) >= ((byte)value)){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Integer.class && ((int)thisValue) >= ((int)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Double.class && ((double)thisValue) >= ((double)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Long.class && ((long)thisValue) >= ((long)value) ){
                                queriedList.add( realmObject);
                            }
                            else if( clazz == Float.class && ((float)thisValue) >= ((float)value) ){
                                queriedList.add( realmObject);
                            }
                        }

                    /*
                    TODO: more conditions to search and update queryMap<realmQueryClass, collection>
                    else if( compare == Compare.contains && clazz == String.class ){


                    }
                    else if( compare == Compare.startsWith && clazz == String.class ){


                    }
                    else if( compare == Compare.endsWith && clazz == String.class ){


                    }*/

                    }
                }

                queryMap.put( realmQueryClass, queriedList);

                return realmQuery;
            }
        };
    }
}