# Notes

Please add here any notes, assumptions and design decisions that might help up understand your though process.

- what happens when an item is subject to multiple discounts? I assumed the most convenient offer for an item chosen in 
 a random order will be applied but I feel that's not ultimately going to be the correct assumption. 
 I am leaving room for flexibility there by using a comparator based sorting and maybe I'll think more later or we think 
 together...
 I don't like to assume this because there could be another product that can be part of a bigger 
 discount than the initially chosen one but is used for a discount for an item appearing earlier in the basket...
 I would get all the discounts regardless of the product and see if they are applicable. I think there can't be too many 
 discounts happening at the same time. But to be sure I will get only the discounts involving products in the basket...
 There is still the following problem, what happens if I have a discount like "5 items - 5GBP discount" and "10 items - 
 9GBP discount". If the basket has 10 items then my algorithm will choose the 9GBP discount whereas applying twice the 
 5GBP one would be more convenient. I think such a discount is unlikely in the real world though, usually the more you 
 buy the bigger the discount. There could still be a situation where we have "5 items of product A - 5GBP discount" and
 "5 items of product B - 5GBP discount" but leaving it like this
- "3 items" means 3 items of the same product or could be mix and match? 
- assuming I have no offers like: "if basket is worth more than 100 GBP, you get 10% off". But adding
different DisountService implementations are possible and possibly even chaining
- I assume weighed items are never in the same special offer with unit items
- I don't like the separation between Products and Weighed Products but I can see some potential scenarios to have it like this
- The discount storage would need to return Discounts of this form:
  * for 2 for 3 on milk of 1.0 it would have to have discountAmount = 1.0 and requiredInBasket ={(milk, 3)}
  * for buy 2 items for 1GBP discountAmount = (cumulated price of the items - 1.0) and requiredInBasket ={(item1, 1), (item2, 1)}
    and it would need to store a Discount for each of the products that participate
  * for buy one kilo of vegetables for half price discountAmount = kilo price / 2 and requiredInBasket ={(veggies, 1.0)}

TODO:
- need to check the scale of big decimals
- refactor exceptions
- add a converter items to products and extract that from Basket 
- separate in packages
- I only wrote a test for dicount service for myself to see if it works, an organized test for that class should be done
