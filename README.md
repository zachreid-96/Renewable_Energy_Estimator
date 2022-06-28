# Renewable Energy Estimator
A program built to 'estimate' how many Solar Panels, Solar Farms, and Wind Turbines needed to produce enough (or more!) than what the US used in 2021.
The US used approximately 4,112 billion kiloWatt Hours (kWH) in 2021.

Each Class is full of comments, some more than others, but most of what needs a comment has one. Here is a short break down of the classes used:

## Classes

**Wind_Turbine**

Houses Wind Turbine Objects. Variables include Height of the Turbine, Arm Length, and Name. Calculated Variables include Watt Rating
(converting MegaWatts (MW) to kiloWatts (kW)), Sweeping Area of the Turbine Arms. Other functionality of this class include estimating the
efficiency of the Turbine, calculating the Potential Power and Output Power of the Turbine.

**State**

Houses the State Objects. Lots of variables packed inside this class, many are named in a self-explanitory method. Essentially this class holds vital
information like Peak Sun Hours (PSH) for each state, which is used in Calculating Power Output for Solar Panels,
Average Wind Speed at 100m above Surface Level, which is used in Calculating Power Output for Wind Turbines, and much more.

**Solar_Panel**

Houses Solar Panel Objects. The two used Solar Panel Types are the 'Residential' 60 celled, and 'Industrial' 72 celled panels. Used to estimate
Power Output (kWH) for a single panel.

**Solar_Farm**

Houses and Creates Solar Farm Objects. The two used Solar Panel Farm Types are 'MicroGrid' which is always constructed with 2 acres of land,
and the 'IndustrialGrid' which is constructed with anywhere between 10-200 acres of land. Other functionality include calculating how many
solar panels would fit within the allocated space (15-30%) of the total area.

**Renewable_Energy**

Does quite a bit, and probably should be split into a few Classes. Main functionalities include calculating how many Solar Farms and/or Wind Turbines
are needed to meet the user specified kWH goal. Other functionalities include writing test results to files to be viewed later. Does not output
to console, only to files.

**Renewable_Energy_GUI**

A Graphical Interface for the Renewable_Energy Class. Functionalities include creating (styling coming soon) the Interface to 'customize' what
Panel Types Solar Farms are filled with, what Types of Solar Layouts, what Wind Turbines to be inlcuded in testing, how many tests to be run in one
sitting, and how many kWH to aim for.

## Other Notes

**Inspiration**

Green Energy is imperative to the survival of the Earth in a long, big picture, sense. 

This is only an estimation tool thrown together by someone who disagreed with someone working for an oil company. 
I am working on refining percentages in regards to how much land can be used in 
Solar Farm, and refining calculations for power output. This is by no means a perfect project, and some equations are not super precise
due to my rounding at certain steps, but that is why this in ONLY an estimator and has been a fun project for me.

**Current Oddities**

Currently in the GUI a 'Wind Turbine (328ft)' is displayed, but disabled due to not having offshore wind speed data avaliable in the states.txt file
to actually be used. An update will be released adding this data and functionality.

**Future Functionalities**

I am exploring other forms of Renewable/Green Energy that can be implemented, like the Solar Mirror Farms, Geothermal Farms, adding more Solar Panel Types,
and other Solar Panel Farm Types, as well as refining existing ones. Styling and colorizing of the GUI is in the works as well, for Linux and Windows
platforms (sorry MacOS), as I currently use Linux Mint and Windows 10 platforms.

**Be Respectful**

Again this is a hobby project of mine, and just an estimator, it has been a fun project to work on. If something is inaccurate, report it as an issue
and please be respectful about it. 

### Thanks for Checking Out my Project!
