# Notes

Please add here any notes, assumptions and design decisions that might help up understand your though process.

- what happens when an item is subject to multiple discounts? I assumed the most convenient offer for an items chosen in 
 the order that they were added will be applied but I feel that's not ultimately going to be the correct assumption. 
 I am leaving room for flexibility there by using a comparator based sorting and maybe I'll think more later or we think 
 together...
- "3 items" means 3 items of the same product or could be mix and match?
- assuming I have no offers like: "if basket is worth more than 100 GBP, you get 10% off". But adding
different DisountService implementations are possible and possibly even chaining
- need to check the scale of big decimals
- refactor exceptions
- add a converter items to products and extract that from Basket 
