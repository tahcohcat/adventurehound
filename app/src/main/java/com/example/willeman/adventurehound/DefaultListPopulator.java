package com.example.willeman.adventurehound;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WILLEMAN on 8/27/2016.
 */
public class DefaultListPopulator {

    private Context context;
    private String dbName;

    public DefaultListPopulator(
        String listDbName,
        Context context)
    {
        this.context = context;
        this.dbName = listDbName;
        createDefaultList();
    }

    public void createDefaultList()
    {
        List<TaskListDocument> items = new ArrayList<>();

        //FairView
        TaskListDocument item =  createNewDoc("FairView WineFarm","http://www.fairview.co.za/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("rating", "3");
        items.add(item);

        //Surfing
        item = createNewDoc("Surfing","http://www.surfschools.co.za/strand/price-list/", context.getResources().getString(R.string.cat_sport_label));
        item.addAttribute("rating", "3");
        item.addAttribute("price", "450");
        items.add(item);

        //SUP
        item = createNewDoc("SUP","http://high-five.co.za/sup-surf-lessons", context.getResources().getString(R.string.cat_sport_label));
        item.addAttribute("rating", "2");
        item.addAttribute("price", "300");
        items.add(item);

        //Koeelbaai Camping
        item = createNewDoc("Koeelbaai Camping","https://www.capetown.gov.za/en/SportRecreation/Pages/KogelBayresort.aspx", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("rating", "4");
        item.addAttribute("price", "150");
        item.addAttribute("weather", "sunny");
        items.add(item);

        //Sutherland
        item = createNewDoc("Star Gazing (Sutherland)","http://www.sutherlandinfo.co.za/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "900");
        item.addAttribute("weather", "winter");
        items.add(item);

        //Phlip&Jaco Boardgame Evening
        item = createNewDoc("Phlip&Jaco Boardgame Evening","", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "0");
        item.addAttribute("weather", "rainy");
        items.add(item);

        //Fanaticus
        item = createNewDoc("Fanaticus","Casual boardgame Saturdays", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "0");
        item.addAttribute("rating", "2");
        items.add(item);

        //Weskus blomme
        item = createNewDoc("West-coast flowers","Early to mid September - http://www.capewestcoast.org/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "400");
        item.addAttribute("rating", "4");
        items.add(item);

        //Colour Run
        item = createNewDoc("Colour run","http://thecolorrun.co.za/cape-town/ 12 Nov 2016", context.getResources().getString(R.string.cat_sport_label));
        item.addAttribute("price", "250");
        item.addAttribute("rating", "4");
        items.add(item);

        //Botmaskop
        item = createNewDoc("Botmaskop sleep-over","Maybe Ettienne and Salome to join?", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "0");
        item.addAttribute("rating", "3");
        items.add(item);

        //Llandudno
        item = createNewDoc("Llandudno","Limited parking. Go early?", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "0");
        item.addAttribute("rating", "4");
        item.addAttribute("weather", "sunny");
        items.add(item);

        //Sushi belugas
        item = createNewDoc("Belugas Sushi","Known for sushi platters. Clement?", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "300");
        item.addAttribute("rating", "3");
        items.add(item);

        //Two Oceans
        item = createNewDoc("Two Oceans Half-marathon","http://www.twooceansmarathon.org.za/", context.getResources().getString(R.string.cat_sport_label));
        item.addAttribute("price", "200");
        item.addAttribute("rating", "4");
        items.add(item);

        //Jewel of India
        item = createNewDoc("Jewel of India","Greenpoint - https://www.zomato.com/capetown/jewel-of-india-green-point?ref_type=subzone&ref_id=64005", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "150");
        item.addAttribute("rating", "3");
        items.add(item);

        //Parkruns
        item = createNewDoc("Parkrun","", context.getResources().getString(R.string.cat_sport_label));
        item.addAttribute("price", "0");
        item.addAttribute("rating", "3");
        items.add(item);

        //KKNK
        item = createNewDoc("Klein Karoo Kunste Fees","www.kknk.co.za", context.getResources().getString(R.string.cat_sport_label));
        item.addAttribute("price", "");
        item.addAttribute("rating", "3");
        items.add(item);

        //PotLuck Club
        item = createNewDoc("Potluck Club","http://thepotluckclub.co.za/", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "400");
        item.addAttribute("rating", "4");
        items.add(item);

        //Planetarium
        item = createNewDoc("Cape Town Planetarium Course","", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("rating", "3");
        items.add(item);

        //3D Imax Cape Gate
        item = createNewDoc("3D Imax Cape Gate","http://www.imax.com/theatres/ster-kinekor-capegate-imax", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "170");
        item.addAttribute("rating", "3");
        items.add(item);

        //Ice Cream Shop Boulders Simonstown
        item = createNewDoc("Ice Cream Shop Boulders Simonstown","", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "170");
        item.addAttribute("rating", "3");
        items.add(item);

        //Explore Top 10 Ice Cream Shops in Cape Town
        item = createNewDoc("Explore Top 10 Ice Cream Shops in Cape Town","http://www.becomingyou.co.za/top-10-ice-cream-shops-in-cape-town/ also visit Boulders beach ice cream shop and The Creamery http://thecreamery.co.za/", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "300");
        items.add(item);

        //Creamery Ice Cream Shop
        item = createNewDoc("The Creamery Ice Cream Shop","http://thecreamery.co.za/", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "100");
        items.add(item);

        //Creamery Ice Cream Shop
        item = createNewDoc("Rockland Theatre",
                "http://www.capetownmagazine.com/theatre/the-theatre-at-the-rockwell-hotel-in-cape-town/143_22_19690",
                context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "350");
        items.add(item);

        //Charleys Bakery
        item = createNewDoc("Charleys Bakery","http://charlysbakery.co.za/", context.getResources().getString(R.string.cat_food_label));
        items.add(item);

        //Jason Bakery
        item = createNewDoc("Breakfast at Jason Bakery","http://www.jasonbakery.com/", context.getResources().getString(R.string.cat_food_label));
        items.add(item);

        //Velvet Cupcakes
        item = createNewDoc("Velvet Cupcakes","http://www.thevelvetcakeco.com/", context.getResources().getString(R.string.cat_food_label));
        items.add(item);

        //Tea Room
        item = createNewDoc("Lady Bonin's Tea Bar","http://ladyboninstea.com/", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "60");
        items.add(item);

        //Hire Bikes Prominade
        item = createNewDoc("Hire Bicycles","V&A waterfront or Seapont prominade - http://www.bicyclecapetown.org/city-guide/bicycle-rental-tours/", context.getResources().getString(R.string.cat_sport_label));
        item.addAttribute("price", "150");
        items.add(item);

        //Galileo Outdoor Cinema
        item = createNewDoc("Galileo Outdoor Cinema","", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "170");
        item.addAttribute("rating", "5");
        items.add(item);

        //BlueRock
        item = createNewDoc("BlueRock","Just to chill there. SUPs. With Tom&Ceris? - http://bluerock.co.za/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "250");
        item.addAttribute("rating", "3");
        item.addAttribute("weather", "sunny");
        items.add(item);

        //Kirstenbosch
        item = createNewDoc("Kirstenbosch Summer Concerts","https://www.webtickets.co.za/events/best-sellers/kirstenbosch-summer-sunset-concerts/1457619558", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "250");
        item.addAttribute("rating", "4");
        item.addAttribute("weather", "sunny");
        items.add(item);

        //Clos Malverne
        item = createNewDoc("Ice Cream & Wine","Clos Malverne. Any other?", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "150");
        item.addAttribute("rating", "4");
        items.add(item);

        //Clos Malverne
        item = createNewDoc("US Conserve Consert","http://www.sun.ac.za/english/faculty/arts/music-konservatorium/concerts", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "50");
        item.addAttribute("rating", "3");
        items.add(item);

        //Jonkershoek stap + 2e Waterval
        item = createNewDoc("Jonkershoek","Walk to 2nd waterfall - http://www.campingandhiking.co.za/jl2/index.php/hikes/day-hikes/second-waterfall-jonkershoek-menu", context.getResources().getString(R.string.cat_sport_label));
        item.addAttribute("price", "150");
        item.addAttribute("rating", "4");
        items.add(item);

        //Jozi Ice Cream
        item = createNewDoc("Pretoria Trip","Jozi ice cream, Jakarandas, Charlotte, Voortrekker monument", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "2000");
        item.addAttribute("rating", "3");
        items.add(item);

        //Camping in Cederberg
        item = createNewDoc("Camping in Cederberg","http://www.cederberg.com/listplaces.php?bt=21", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "1000");
        item.addAttribute("rating", "3");
        items.add(item);

        //Expos
        item = createNewDoc("Expos","Good food & Wine, Other?", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "500");
        item.addAttribute("rating", "3");
        items.add(item);

        //Hudson's Burger
        item = createNewDoc("Hudsons Burgers","", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "200");
        items.add(item);

        //Crystal Pools
        item = createNewDoc("Crystal Pools","Get Permit first - http://showme.co.za/helderberg/tourism/crystal-pools-hiking-trail-steenbras-river-gorge/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "60");
        item.addAttribute("rating", "4");
        item.addAttribute("weather", "sunny");
        items.add(item);

        //Iziko
        item = createNewDoc("BBC Nature Photograper Expo","Iziko Museum - http://www.iziko.org.za/calendar/exhibitions/current", context.getResources().getString(R.string.cat_art_label));
        item.addAttribute("price", "50");
        items.add(item);

        //Artscape
        item = createNewDoc("Artscape","http://www.artscape.co.za/shows/whats-on-now/", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "350");
        items.add(item);

        //Ballet Show
        item = createNewDoc("Ballet show","http://www.capetowncityballet.org.za/", context.getResources().getString(R.string.cat_art_label));
        item.addAttribute("price", "350");
        items.add(item);

        //Newlands Cricket Match
        item = createNewDoc("Newlands Cricket Match","http://www.newlandscricket.com/events--fixtures.html", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "240");
        items.add(item);

        //Go to a comedy club
        item = createNewDoc("Go to a comedy club","http://capetowncomedy.com/ ", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "150");
        items.add(item);

        //Rooftop Cinema
        item = createNewDoc("Rooftop cinema","http://www.whatsonincapetown.com/post/pink-flamingo-rooftop-cinema/", context.getResources().getString(R.string.cat_entertainment_label));
        item.addAttribute("price", "100");
        items.add(item);

        //Elandsbaai
        item = createNewDoc("Muisbosskerm","http://www.muisbosskerm.co.za/ 280 kilmeters drive (Lambertsbay)", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("rating", "4");
        items.add(item);

        //Starlight Diner
        item = createNewDoc("Sunset at starlight diner","https://www.zomato.com/capetown/starlite-diner-bellvile", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "75");
        items.add(item);

        //Eerste Rivier Tube Riding
        item = createNewDoc("Eerste Rivier tube riding","Do in winter after heavy downpour as the river levels needs to have risen. Need big tube and wetsuits", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "0");
        items.add(item);

        //Kamers vol geskenke
        item = createNewDoc("Kamers vol geskenke","1-6 Nov 2016 Stellenbosch - http://kamersvol.com/", context.getResources().getString(R.string.cat_art_label));
        item.addAttribute("price", "");
        items.add(item);

        //Skydiving
        item = createNewDoc("Skydiving","http://www.skydivecapetown.co.za/prices.aspx http://www.mothercityskydiving.co.za/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "2300");
        items.add(item);

        //Dune boarding
        item = createNewDoc("Dune boarding","http://sandboardingcapetown.com/tours-and-trips.php", context.getResources().getString(R.string.cat_sport_label));
        item.addAttribute("price", "600");
        items.add(item);

        //Cango caves
        item = createNewDoc("Cango caves","http://www.cango-caves.co.za/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "150");
        items.add(item);

        //Garden Route
        item = createNewDoc("The Garden Route","http://www.gardenroute.co.za/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "2000");
        items.add(item);

        //Moyo stellenbosch
        item = createNewDoc("Moyo stellenbosch","Tree tops", context.getResources().getString(R.string.cat_food_label));
        item.addAttribute("price", "300");
        items.add(item);

        //Robben Island
        item = createNewDoc("Robben Island","http://www.robben-island.org.za/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "320");
        items.add(item);

        //Red Top Bus
        item = createNewDoc("Red Top Bus","https://www.citysightseeing.co.za/cape-town", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "170");
        items.add(item);

        //Orange River Canoeing
        item = createNewDoc("Orange River Canoeing","http://felixunite.com/rates/", context.getResources().getString(R.string.cat_adventure_label));
        item.addAttribute("price", "4000");
        items.add(item);

        item = createNewDoc("Fish River Canyon","https://www.expertafrica.com/namibia/fish-river-canyon", context.getResources().getString(R.string.cat_adventure_label));
        items.add(item);

        http://www.becomingyou.co.za/top-10-ice-cream-shops-in-cape-town/

        //createActivityDocWithAttributes(database, "Botmaskop", "Sleepover. Will need ideally 4 people",
        // Arrays.asList("category:adventure", "weather:sunny", "env:outdoors", "energy_level:mediumhigh", "cost:1"), null);

        //item = createNewDoc("","", context.getResources().getString(R.string.cat_sport_label));
        //item.addAttribute("rating", "");
        //item.addAttribute("price","");
        //items.add(item);

        addListToDataBase(items);
    }

    public void addListToDataBase(List<TaskListDocument> items)
    {
        ItemWriter writer = new ItemWriter();

        for (TaskListDocument item : items) {
            if (!writer.writeItem(this.context, this.dbName, item)) {
                Log.e("DEFAULT_LIST_POPULATOR", "Could not write item : '" + item.getActivity() + "' to database");
                assert (false);
            }
        }
    }

    private TaskListDocument createNewDoc(String activity, String details, String category)
    {
        Map attributes = new HashMap<String,String>();
        attributes.put("category",category);
        return new TaskListDocument("", activity, details, false, new ArrayList<String>(), attributes);
    }
}
